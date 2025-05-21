package com.example.internetbanking.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.internetbanking.AppScreen
import com.example.internetbanking.data.CustomerUiState
import com.example.internetbanking.data.TransactionDetail
import com.example.internetbanking.data.TransactionRecord
import com.example.internetbanking.data.User
import com.example.internetbanking.ui.shared.addDocumentToCollection
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.shared.generateUniqueTransactionId
import com.example.internetbanking.ui.shared.updateUserFieldByAccountId
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal

class CustomerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()
    private val auth: FirebaseAuth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

    // OBSERVING EXISTING CARD TYPE
    var hasSaving by mutableStateOf(false)
        private set
    var hasMortgage by mutableStateOf(false)
        private set

    fun observeSubAccountsExistence(accountId: String) {
        val savingRef = db.collection("saving")
            .whereEqualTo("accountId", accountId)
            .limit(1)
        val mortgageRef = db.collection("mortgage")
            .whereEqualTo("accountId", accountId)
            .limit(1)

        savingRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }
            hasSaving = snapshot != null && !snapshot.isEmpty
        }
        mortgageRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }
            hasMortgage = snapshot != null && !snapshot.isEmpty
        }
    }

    private suspend fun checkSubAccountsExistence(accountId: String): Pair<Boolean, Boolean> {
        return try {
            val savingSnapshot = db.collection("saving")
                .whereEqualTo("accountId", accountId)
                .limit(1)
                .get()
                .await()
            val mortgageSnapshot = db.collection("mortgage")
                .whereEqualTo("accountId", accountId)
                .limit(1)
                .get()
                .await()

            val savingExists = !savingSnapshot.isEmpty
            val mortgageExists = !mortgageSnapshot.isEmpty

            Pair(savingExists, mortgageExists)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(false, false)
        }
    }

    // OBSERVING CARD BALANCE
    fun observeBalance(accountId: String) {
        // Observe checking balance
        db.collection("checking")
            .whereEqualTo("accountId", accountId)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("BalanceObserver", "Failed to observe checking: ${error.message}")
                    return@addSnapshotListener
                }

                val checkingBalance =
                    snapshot?.documents?.firstOrNull()?.get("balance")?.let { value ->
                        when (value) {
                            is Number -> BigDecimal.valueOf(value.toDouble())
                            is String -> value.toBigDecimalOrNull() ?: BigDecimal.ZERO
                            else -> BigDecimal.ZERO
                        }
                    } ?: BigDecimal.ZERO

                _uiState.update { it.copy(checkingBalance = checkingBalance) }
            }

        // Observe saving balance
        db.collection("saving")
            .whereEqualTo("accountId", accountId)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("BalanceObserver", "Failed to observe saving: ${error.message}")
                    return@addSnapshotListener
                }

                val savingBalance =
                    snapshot?.documents?.firstOrNull()?.get("balance")?.let { value ->
                        when (value) {
                            is Number -> BigDecimal.valueOf(value.toDouble())
                            is String -> value.toBigDecimalOrNull() ?: BigDecimal.ZERO
                            else -> BigDecimal.ZERO
                        }
                    } ?: BigDecimal.ZERO

                _uiState.update { it.copy(savingBalance = savingBalance) }
            }
    }

    // LOAD CUSTOMER INFORMATION
    fun loadCustomerInformation(currentUser: User) {
        val accountId = currentUser.accountId
        viewModelScope.launch {
            // Kiểm tra sự tồn tại các sub-accounts
            val (savingExists, mortgageExists) = checkSubAccountsExistence(accountId)
            hasSaving = savingExists
            hasMortgage = mortgageExists

            // Truy vấn checking để lấy card number
            val checkingSnapshot = db.collection("checking")
                .whereEqualTo("accountId", accountId)
                .limit(1)
                .get()
                .await()
            val checkingCardNumber =
                checkingSnapshot.documents.firstOrNull()?.getString("cardNumber") ?: ""

            // Truy vấn saving nếu tồn tại
            val savingCardNumber = if (savingExists) {
                db.collection("saving")
                    .whereEqualTo("accountId", accountId)
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()
                    ?.getString("cardNumber") ?: ""
            } else ""

            // Truy vấn mortgage nếu tồn tại
            val mortgageCardNumber = if (mortgageExists) {
                db.collection("mortgage")
                    .whereEqualTo("accountId", accountId)
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()
                    ?.getString("cardNumber") ?: ""
            } else ""

            // Cập nhật state
            _uiState.update { currentState ->
                currentState.copy(
                    account = currentUser,
                    checkingCardNumber = checkingCardNumber,
                    savingCardNumber = savingCardNumber,
                    mortgageCardNumber = mortgageCardNumber
                )
            }

            // Bắt đầu lắng nghe các dữ liệu realtime
            observeSubAccountsExistence(accountId)
            observeBalance(accountId)
        }
    }

    // EDIT INFORMATION
    // Error Messages
    var nameErrorMessage by mutableStateOf("")
        private set
    var genderErrorMessage by mutableStateOf("")
        private set
    var phoneNumberErrorMessage by mutableStateOf("")
        private set
    var birthdayErrorMessage by mutableStateOf("")
        private set
    var addressErrorMessage by mutableStateOf("")
        private set

    fun clearErrorMessage() {
        nameErrorMessage = ""
        genderErrorMessage = ""
        phoneNumberErrorMessage = ""
        birthdayErrorMessage = ""
        addressErrorMessage = ""
    }

    // Validate User Edit Input
    fun validateEditInput(
        fullName: String,
        gender: String,
        phoneNumber: String,
        birthday: String,
        address: String
    ): Boolean {
        var isValid = true

        // Full name
        if (fullName.isEmpty()) {
            nameErrorMessage = "Full name is required"
            isValid = false
        } else {
            nameErrorMessage = ""
        }

        // Gender
        if (gender.isEmpty()) {
            genderErrorMessage = "Gender is required"
            isValid = false
        } else {
            genderErrorMessage = ""
        }

        // Phone number
        if (phoneNumber.isEmpty()) {
            phoneNumberErrorMessage = "Phone number is required"
            isValid = false
        } else if (phoneNumber.length != 10 && phoneNumber.length != 11) {
            phoneNumberErrorMessage = "Invalid phone number"
            isValid = false
        } else {
            phoneNumberErrorMessage = ""
        }

        // Birthday
        if (birthday.isEmpty()) {
            birthdayErrorMessage = "Birthday is required"
            isValid = false
        } else {
            birthdayErrorMessage = ""
        }

        // Address
        if (address.isEmpty()) {
            addressErrorMessage = "Address is required"
            isValid = false
        } else {
            addressErrorMessage = ""
        }

        return isValid
    }

    // User Save New Information Event
    fun onEditSaveClick(
        context: Context,
        navController: NavHostController,
        loginViewModel: LoginViewModel,
        fullName: String,
        gender: String,
        phoneNumber: String,
        birthday: String,
        address: String,
    ) {
        if (validateEditInput(fullName, gender, phoneNumber, birthday, address)) {
            val accountId = uiState.value.account.accountId
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "fullName",
                newValue = fullName,
                onSuccess = {},
                onFailure = { e ->
                    Toast.makeText(
                        context,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "gender",
                newValue = gender,
                onSuccess = {},
                onFailure = { e ->
                    Toast.makeText(
                        context,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "phoneNumber",
                newValue = phoneNumber,
                onSuccess = {},
                onFailure = { e ->
                    Toast.makeText(
                        context,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "birthday",
                newValue = birthday,
                onSuccess = {},
                onFailure = { e ->
                    Toast.makeText(
                        context,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "address",
                newValue = address,
                onSuccess = {},
                onFailure = { e ->
                    Toast.makeText(
                        context,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            loginViewModel.updateCurrentCustomerInformation(
                fullName,
                gender,
                phoneNumber,
                birthday,
                address
            )
            Toast.makeText(context, "Information updated successfully", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    // Transaction History
    fun onTransactionHistoryClick(
        cardNumber: String,
        navController: NavHostController
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                currentCardView = cardNumber
            )
        }
        navController.navigate(AppScreen.TransactionHistory.name)
    }

    // Store the full transaction history internally
    private var fullTransactionHistory: List<TransactionRecord> = emptyList()

    // LOAD HISTORY
    fun loadTransactionHistory(cardNumber: String) {
        viewModelScope.launch {
            try {
                val sourceSnapshot = db.collection("transactionHistories")
                    .whereEqualTo("sourceCard", cardNumber)
                    .get().await()

                val destSnapshot = db.collection("transactionHistories")
                    .whereEqualTo("destinationCard", cardNumber)
                    .get().await()

                val sourceTransactions = parseTransactions(sourceSnapshot.documents)
                val destTransactions = parseTransactions(destSnapshot.documents)

                val allTransactions = (sourceTransactions + destTransactions)
                    .distinctBy { it.transactionId }

                fullTransactionHistory = allTransactions

                _uiState.update { it.copy(transactionHistory = allTransactions) }

            } catch (e: Exception) {
                Log.e("LoadTransactionHistory", "Error loading history: ${e.message}")
            }
        }
    }

    private fun parseTransactions(documents: List<DocumentSnapshot>): List<TransactionRecord> {
        return documents.mapNotNull { doc ->
            try {
                val timestampValue = doc.get("timestamp")
                val timestamp = when (timestampValue) {
                    is Number -> timestampValue.toLong()
                    is String -> timestampValue.toLongOrNull() ?: 0L
                    is com.google.firebase.Timestamp -> timestampValue.toDate().time
                    else -> 0L
                }

                val amountValue = doc.get("amount")
                val amount = when (amountValue) {
                    is Number -> BigDecimal.valueOf(amountValue.toDouble())
                    is String -> amountValue.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    else -> BigDecimal.ZERO
                }

                val feeValue = doc.get("fee")
                val fee = when (feeValue) {
                    is Number -> BigDecimal.valueOf(feeValue.toDouble())
                    is String -> feeValue.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    else -> BigDecimal.ZERO
                }

                TransactionRecord(
                    transactionId = doc.id,
                    amount = amount,
                    fee = fee,
                    timestamp = timestamp,
                    sourceCard = doc.get("sourceCard").toString(),
                    destinationCard = doc.get("destinationCard").toString(),
                    type = doc.getString("type") ?: "",
                )
            } catch (e: Exception) {
                Log.e("ParseTransaction", "Failed parsing transaction: ${e.message}")
                null
            }
        }
    }

    // Filter transactions by type and date range
    fun filterTransactions(
        filterType: String,
        selectedCardNumber: String,
        fromDate: Long? = null,
        toDate: Long? = null
    ) {
        val normalizedType = filterType.trim().lowercase()

        val filteredTransactions = fullTransactionHistory.filter { transaction ->
            if (transaction.timestamp <= 0L) return@filter false

            val typeMatch = when (normalizedType) {
                "in" -> transaction.destinationCard == selectedCardNumber
                "out" -> transaction.sourceCard == selectedCardNumber
                "all", "" -> transaction.sourceCard == selectedCardNumber || transaction.destinationCard == selectedCardNumber
                else -> true
            }

            val dateMatch = when {
                fromDate != null && toDate != null -> transaction.timestamp in fromDate..toDate
                fromDate != null -> transaction.timestamp >= fromDate
                toDate != null -> transaction.timestamp <= toDate
                else -> true
            }

            typeMatch && dateMatch
        }

        _uiState.update { it.copy(transactionHistory = filteredTransactions) }
    }

    // View Transaction Detail
    fun onTransactionDetailClick(
        transaction: TransactionRecord,
        navController: NavHostController
    ) {
        _uiState.update { it.copy(transactionHistoryToView = transaction) }
        // TODO: NAVIGATE TO THE SCREEN

    }

    // Transfer Continue Button Click
    fun onContinueTransferClick(
        bank: String, sourceCard: String,
        amount: BigDecimal, content: String, category: String,
        destinationCard: String = "",
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val newTransactionId = generateUniqueTransactionId()
            val timestamp: Long = System.currentTimeMillis()
            val cate = if (category.isEmpty()) "Transfer" else category
            val summaryContent = "[$cate] ${formatCurrencyVN(amount)} has been transferred by $sourceCard to ${destinationCard}($bank) with message:\"$content\""

            val newTransferRecord = TransactionRecord(
                transactionId = newTransactionId,
                amount = amount,
                timestamp = timestamp,
                sourceCard = sourceCard,
                destinationCard = destinationCard,
                type = "Transfer"
            )
            val newTransferDetail = TransactionDetail(
                transaction = newTransferRecord,
                content = summaryContent,
                category = cate
            )

            _uiState.update { currentState ->
                currentState.copy(
                    checkingCurrentTransfer = newTransferDetail
                )
            }
            navController.navigate(AppScreen.Confirm.name)
        }
    }

    // Transfer Event
    fun getCardDocRef(cardNumber: String): DocumentReference {
        val collections = listOf("checking", "saving", "mortgage")
        for (collection in collections) {
            val docRef = db.collection(collection).document(cardNumber)
            return docRef
        }
        throw IllegalArgumentException("Card number $cardNumber not found in any collection")
    }

    suspend fun transferAmountBetweenAccounts(
        sourceCardNumber: String,
        destinationCardNumber: String,
        amount: BigDecimal
    ): Boolean {
        return try {
            db.runTransaction { transaction ->

                val sourceDocRef = getCardDocRef(sourceCardNumber)
                val destDocRef = getCardDocRef(destinationCardNumber)

                // Check source
                val sourceSnapshot = transaction.get(sourceDocRef)
                val sourceBalance = sourceSnapshot.getString("balance")?.toBigDecimalOrNull()

                val newSourceBalance = sourceBalance?.minus(amount)
                transaction.update(sourceDocRef, "balance", newSourceBalance)

                // Update destination
                val destSnapshot = transaction.get(destDocRef)
                val destBalance = destSnapshot.getString("balance")?.toBigDecimalOrNull()

                val newDestBalance = destBalance?.plus(amount)
                transaction.update(destDocRef, "balance", newDestBalance)

            }.await()
            true
        } catch (e: Exception) {
            Log.e("Transfer", "Error during transfer: ${e.message}")
            false
        }
    }


    // Password Confirm
    fun onPasswordConfirm(
        transactionDetail: TransactionDetail,
        password: String,
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val email = uiState.value.account.email
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                val sourceCard = transactionDetail.transaction.sourceCard
                val destCard = transactionDetail.transaction.destinationCard
                val amount = transactionDetail.transaction.amount

                val success = transferAmountBetweenAccounts(sourceCard, destCard, amount)
                if (success) {
                    addDocumentToCollection(
                        collectionName = "transferDetails",
                        data = mapOf(
                            "transactionId" to transactionDetail.transaction.transactionId,
                            "amount" to amount,
                            "fee" to transactionDetail.transaction.fee,
                            "timestamp" to transactionDetail.transaction.timestamp,
                            "sourceCard" to sourceCard,
                            "destinationCard" to destCard,
                            "type" to transactionDetail.transaction.type,
                            "content" to transactionDetail.content,
                            "category" to transactionDetail.category
                        ),
                        documentId = transactionDetail.transaction.transactionId,
                        onSuccess = {
                            Toast.makeText(context, "Transfer successful", Toast.LENGTH_SHORT).show()
                            navController.navigate(AppScreen.CustomerHome.name)
                        }
                    )
                } else {
                    Toast.makeText(context, "Transfer failed", Toast.LENGTH_SHORT).show()
                }

            } catch (_: Exception) {
                Toast.makeText(context, "Confirm failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}