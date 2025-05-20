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
import com.example.internetbanking.data.TransactionRecord
import com.example.internetbanking.data.User
import com.example.internetbanking.ui.shared.addDocumentToCollection
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.shared.generateUniqueTransactionId
import com.example.internetbanking.ui.shared.updateUserFieldByAccountId
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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

                val checkingBalance = snapshot?.documents?.firstOrNull()?.get("balance")?.let { value ->
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

                val savingBalance = snapshot?.documents?.firstOrNull()?.get("balance")?.let { value ->
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
            val checkingCardNumber = checkingSnapshot.documents.firstOrNull()?.getString("cardNumber") ?: ""

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
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "gender",
                newValue = gender,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "phoneNumber",
                newValue = phoneNumber,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "birthday",
                newValue = birthday,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            updateUserFieldByAccountId(
                accountId = accountId,
                fieldName = "address",
                newValue = address,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            loginViewModel.updateCurrentCustomerInformation(fullName, gender, phoneNumber, birthday, address)
            Toast.makeText(context, "Information updated successfully", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    // Transaction History
    // Store the full transaction history internally
    private var fullTransactionHistory: List<TransactionRecord> = emptyList()
    fun observeTransactionHistory(accountId: String) {
        val collectionRef = db.collection("transactionHistories")
            .whereEqualTo("accountId", accountId)

        collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("TransactionHistoryObserver", "Listen failed: ${error.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val transactions = snapshot.documents.mapNotNull { doc ->
                    try {
                        val timestampValue = doc.get("timestamp")
                        val timestamp = when (timestampValue) {
                            is Number -> timestampValue.toLong()
                            is String -> timestampValue.toLongOrNull() ?: 0L
                            is com.google.firebase.Timestamp -> timestampValue.toDate().time
                            else -> {
                                Log.w("TransactionHistoryObserver", "Invalid timestamp format for document ${doc.id}")
                                0L
                            }
                        }

                        TransactionRecord(
                            transactionId = doc.getString("transactionId") ?: "",
                            content = doc.getString("content") ?: "",
                            amount = doc.getString("amount")?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                            fee = doc.getString("fee")?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                            type = doc.getString("type") ?: "",
                            timestamp = timestamp
                        )
                    } catch (e: Exception) {
                        Log.e("TransactionHistoryObserver", "Error parsing document ${doc.id}: ${e.message}")
                        null
                    }
                }
                fullTransactionHistory = transactions
                _uiState.update { it.copy(transactionHistory = transactions) }
            }
        }
    }

    // Filter transactions by type and date range
    fun filterTransactions(
        filterType: String,
        fromDate: Long? = null,
        toDate: Long? = null
    ) {
        val filteredTransactions = fullTransactionHistory.filter { transaction ->
            // Filter by type
            val typeMatch = when (filterType) {
                "In" -> transaction.type == "In"
                "Out" -> transaction.type == "Out"
                "All" -> true
                else -> true
            }

            // Filter by date range
            val dateMatch = when {
                fromDate != null && toDate != null -> {
                    transaction.timestamp in fromDate..toDate
                }
                fromDate != null -> {
                    transaction.timestamp >= fromDate
                }
                toDate != null -> {
                    transaction.timestamp <= toDate
                }
                else -> true
            }

            typeMatch && dateMatch
        }
        _uiState.update { it.copy(transactionHistory = filteredTransactions) }
    }

    // View Transaction Detail
    fun onTransactionHistoryClick(
        transaction: TransactionRecord,
        navController: NavHostController
    ) {
        _uiState.update {
            it.copy(
                transactionHistoryToView = transaction
            )
        }
        // TODO: NAVIGATE TO THE SCREEN
        // navController.navigate(AppScreen..name)
    }

    // Transfer
    fun onContinueTransferClick(
        navController: NavHostController,
        bank: String,
        card: String,
        amount: BigDecimal,
        content: String,
        category: String,
        beneficiaryAccount: String
    ) {
        val cate = if (category.isEmpty()) "Transfer" else category
        viewModelScope.launch {
            val summaryContent = "[$cate] ${formatCurrencyVN(amount)}đ has been transferred with the content \"$content\" to $card - $bank "
            val transactionId = generateUniqueTransactionId()
            val newTransactionRecord = TransactionRecord(
                transactionId = transactionId,
                content = summaryContent,
                amount = amount,
                type = "Out",
                timestamp = System.currentTimeMillis(),
                beneficiaryAccount = beneficiaryAccount
            )
            _uiState.update { currentState ->
                currentState.copy(
                    checkingCurrentTransfer = newTransactionRecord
                )
            }
            navController.navigate(AppScreen.Confirm.name)
        }
    }

    // Transfer Event
    fun transferEvent(
        senderAccountId: String,
        receiverCardNumber: String,
        amount: BigDecimal,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val db = FirebaseFirestore.getInstance()

        viewModelScope.launch {
            try {
                val receiverQuerySnapshot = db.collection("users")
                    .whereEqualTo("checking.cardNumber", receiverCardNumber)
                    .limit(1)
                    .get()
                    .await()

                if (receiverQuerySnapshot.isEmpty) {
                    throw IllegalArgumentException("Receiver not found")
                }

                val receiverDoc = receiverQuerySnapshot.documents[0]

                db.runTransaction { transaction ->
                    val senderRef = db.collection("users").document(senderAccountId)
                    val senderSnapshot = transaction.get(senderRef)

                    val senderBalance = senderSnapshot.getDouble("checking.balance") ?: 0.0
                    val newSenderBalance = senderBalance - amount.toDouble()
                    if (newSenderBalance < 0) throw IllegalArgumentException("Insufficient funds")

                    transaction.update(senderRef, "checking.balance", newSenderBalance)

                    val receiverRef = receiverDoc.reference
                    val receiverBalance = receiverDoc.getDouble("checking.balance") ?: 0.0
                    val newReceiverBalance = receiverBalance + amount.toDouble()

                    transaction.update(receiverRef, "checking.balance", newReceiverBalance)
                }.addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener {
                    onFailure(it)
                }

            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    // Confirm Password
    fun confirmPassword(
        context: Context,
        currentTransferRecord: TransactionRecord,
        accountId: String,
        password: String,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            try {
                val document = db.collection("users").document(accountId).get().await()
                val email = document.getString("email") ?: ""

                // Success login
                auth.signInWithEmailAndPassword(email, password).await()

                addDocumentToCollection(
                    collectionName = "transactionHistories",
                    data = mapOf(
                        "transactionId" to currentTransferRecord.transactionId,
                        "accountId" to accountId,
                        "amount" to currentTransferRecord.amount,
                        "content" to currentTransferRecord.content,
                        "fee" to currentTransferRecord.fee,
                        "timestamp" to currentTransferRecord.timestamp,
                        "type" to currentTransferRecord.type,
                        "beneficiaryAccount" to currentTransferRecord.beneficiaryAccount
                    ),
                    documentId = currentTransferRecord.transactionId,
                    onSuccess = {
                        transferEvent(
                            senderAccountId = accountId,
                            receiverCardNumber = currentTransferRecord.beneficiaryAccount,
                            amount = currentTransferRecord.amount,
                            onSuccess = {
                                Toast.makeText(context, "Transfer successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate(AppScreen.CustomerHome.name)
                            },
                            onFailure = { Toast.makeText(context, "Cannot transfer money", Toast.LENGTH_SHORT).show() }
                        )
                    },
                    onFailure = { Toast.makeText(context, "Cannot process Firebase data", Toast.LENGTH_SHORT).show() }
                )
            } catch (_: Exception) {
                Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}