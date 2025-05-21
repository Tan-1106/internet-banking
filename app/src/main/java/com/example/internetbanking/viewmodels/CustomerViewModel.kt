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
import com.example.internetbanking.Service
import com.example.internetbanking.data.Bill
import com.example.internetbanking.data.CustomerUiState
import com.example.internetbanking.data.TransactionDetail
import com.example.internetbanking.data.TransactionRecord
import com.example.internetbanking.data.User
import com.example.internetbanking.ui.shared.addDocumentToCollection
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.shared.generateUniqueCardNumber
import com.example.internetbanking.ui.shared.generateUniqueTransactionId
import com.example.internetbanking.ui.shared.getFieldValueFromDocument
import com.example.internetbanking.ui.shared.getMillisAfterMonths
import com.example.internetbanking.ui.shared.updateFieldInDocument
import com.example.internetbanking.ui.shared.updateUserFieldByAccountId
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
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

    // OBSERVE CARD NUMBER
    var checkingCardNumber by mutableStateOf("")
        private set
    var savingCardNumber by mutableStateOf("")
        private set
    var mortgageCardNumber by mutableStateOf("")
        private set

    fun observeCardNumbers(accountId: String) {
        // Observe checking card
        db.collection("checking")
            .whereEqualTo("accountId", accountId)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CardObserver", "Checking card: ${error.message}")
                    return@addSnapshotListener
                }

                val card = snapshot?.documents?.firstOrNull()?.getString("cardNumber") ?: ""
                checkingCardNumber = card
            }

        // Observe saving card
        db.collection("saving")
            .whereEqualTo("accountId", accountId)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CardObserver", "Saving card: ${error.message}")
                    return@addSnapshotListener
                }

                val card = snapshot?.documents?.firstOrNull()?.getString("cardNumber") ?: ""
                savingCardNumber = card
            }

        // Observe mortgage card
        db.collection("mortgage")
            .whereEqualTo("accountId", accountId)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CardObserver", "Mortgage card: ${error.message}")
                    return@addSnapshotListener
                }

                val card = snapshot?.documents?.firstOrNull()?.getString("cardNumber") ?: ""
                mortgageCardNumber = card
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
            observeCardNumbers(accountId)
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
                    is Timestamp -> timestampValue.toDate().time
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
    fun onContinueTransactionClick(
        bank: String = "", amount: BigDecimal = BigDecimal.ZERO, fee: BigDecimal = BigDecimal.ZERO,
        sourceCard: String = "", destinationCard: String = "",
        type: String = "", startTime: String = "", seats: List<String> = emptyList(),
        destinationPhoneNumber: String = "", network: String = "",
        customerCode: String = "", billType: String = "", provider: String = "",
        flightProvider: String = "", numberOfPassengers: Int = 0,
        movieName: String = "", cinema: String = "",
        hotelName: String = "", room: String = "",
        content: String = "", category: String = "",
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val newTransactionId = generateUniqueTransactionId()
            val timestamp: Long = System.currentTimeMillis()
            val cate = if (category.isEmpty()) "Transaction" else category
            val summaryContent =
                "[$cate] ${formatCurrencyVN(amount)} has been transferred by $sourceCard to ${destinationCard}($bank) with message:\"$content\""

            var newTransferRecord = TransactionRecord(
                transactionId = newTransactionId,
                amount = amount,
                fee = fee,
                timestamp = timestamp,
                sourceCard = sourceCard,
                destinationCard = destinationCard,
                type = type,

                startTime = startTime,
                seats = seats,

                destinationPhoneNumber = destinationPhoneNumber,
                network = network,

                billType = billType,
                customerCode = customerCode,
                provider = provider,

                flightProvider = flightProvider,
                numberOfPassengers = numberOfPassengers,

                movieName = movieName,
                cinema = cinema,

                hotelName = hotelName,
                room = room
            )
//            if (type == Service.Paybill.name) {
//
//                newTransferRecord = newTransferRecord.copy(
//                    amount = uiState.value.currentPayBill?.amount ?: BigDecimal.ZERO,
//                    destinationCard = uiState.value.currentPayBill?.cardNumber ?: ""
//                )
//            }
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

    // Transfer Money
    fun transferBetweenCard(
        sourceCard: String,
        destinationCard: String,
        amount: BigDecimal
    ) {
        suspend fun findAccount(cardNumber: String): DocumentReference? {
            val checkingDoc = db.collection("checking").document(cardNumber)
            val savingDoc = db.collection("saving").document(cardNumber)

            val checkingSnap = checkingDoc.get().await()
            if (checkingSnap.exists()) return checkingDoc

            val savingSnap = savingDoc.get().await()
            return if (savingSnap.exists()) savingDoc else null
        }

        viewModelScope.launch {
            val sourceRef = findAccount(sourceCard)
            val destRef = findAccount(destinationCard)
            if (sourceRef == null || destRef == null) {
                Log.e(
                    TAG,
                    "Invalid source or destination account. sourceRef=$sourceRef, destRef=$destRef"
                )
                return@launch
            }

            db.runTransaction { transactionFirebase ->
                val sourceSnap = transactionFirebase.get(sourceRef)
                val destSnap = transactionFirebase.get(destRef)

                fun extractBalance(snapshot: DocumentSnapshot): BigDecimal {
                    return snapshot.get("balance")?.let { value ->
                        when (value) {
                            is Number -> BigDecimal.valueOf(value.toDouble())
                            is String -> value.toBigDecimalOrNull() ?: BigDecimal.ZERO
                            else -> BigDecimal.ZERO
                        }
                    } ?: BigDecimal.ZERO
                }

                val sourceBalance = extractBalance(sourceSnap)
                val destBalance = extractBalance(destSnap)

                val newSourceBalance = (sourceBalance - amount).toDouble()
                val newDestBalance = (destBalance + amount).toDouble()

                transactionFirebase.update(sourceRef, "balance", newSourceBalance)
                transactionFirebase.update(destRef, "balance", newDestBalance)
            }
        }
    }

    companion object {
        private const val TAG = "TransferConfirm"
    }

    fun passwordConfirm(
        transactionDetail: TransactionDetail,
        password: String,
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val email = uiState.value.account.email

            try {
                auth.signInWithEmailAndPassword(email, password).await()

                val transaction = uiState.value.checkingCurrentTransfer.transaction

                val transactionId = transaction.transactionId
                val amount = transaction.amount
                val fee = transaction.fee
                val timestamp = transaction.timestamp
                val sourceCard = transaction.sourceCard
                val destinationCard = transaction.destinationCard
                val type = transaction.type

                val startTime = transaction.startTime
                val seats = transaction.seats

                val destinationPhoneNumber = transaction.destinationPhoneNumber
                val network = transaction.network

                val customerCode = transaction.customerCode
                val provider = transaction.provider

                val flightProvider = transaction.flightProvider
                val numberOfPassengers = transaction.numberOfPassengers

                val movieName = transaction.movieName
                val cinema = transaction.cinema

                val hotelName = transaction.hotelName
                val room = transaction.room

                val content = transactionDetail.content
                val category = transactionDetail.category
                val totalDeduct = amount + fee

                transferBetweenCard(sourceCard, destinationCard, totalDeduct)
                    val history = mapOf(
                        "amount" to amount.toDouble(),
                        "fee" to fee.toDouble(),
                        "sourceCard" to sourceCard,
                        "destinationCard" to destinationCard,
                        "timestamp" to timestamp,
                        "type" to type
                    )
                if (type == Service.Transfer.name) {
                    val detail = mapOf(
                        "transactionId" to transactionId,
                        "amount" to amount.toDouble(),
                        "fee" to fee.toDouble(),
                        "timestamp" to timestamp,
                        "sourceCard" to sourceCard,
                        "destinationCard" to destinationCard,
                        "type" to type,
                        "content" to content,
                        "category" to category
                    )

                    addDocumentToCollection(
                        collectionName = "transferDetails",
                        data = detail,
                        documentId = transactionId,
                        onSuccess = {}
                    )
                    addDocumentToCollection(
                        collectionName = "transactionHistories",
                        data = history,
                        documentId = transactionId,
                        onSuccess = {
                            Toast.makeText(context, "Transaction successful", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                        }
                    )
                } else if (type == Service.Paybill.name) {


                    val history = mapOf(
                        "amount" to amount.toDouble(),
                        "fee" to fee.toDouble(),
                        "sourceCard" to sourceCard,
                        "destinationCard" to destinationCard,
                        "timestamp" to System.currentTimeMillis(),
                        "type" to type
                    )
                    updateFieldInDocument(
                        collectionName = "bills",
                        documentId = customerCode,
                        fieldName = "status",
                        newValue = true
                    )
                    updateFieldInDocument(
                        collectionName = "bills",
                        documentId = customerCode,
                        fieldName = "transactionId",
                        newValue = transactionId
                    )
                    addDocumentToCollection(
                        collectionName = "transactionHistories",
                        data = history,
                        documentId = transactionId,
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Transaction successful",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            navController.navigate(AppScreen.CustomerHome.name) {
                                popUpTo(AppScreen.CustomerHome.name) {
                                    inclusive = true
                                }
                            }
                        }
                    )


                } else if (type == "Phone") {

                } else if (type == "Flight") {

                } else if (type == "Movie") {

                } else if (type == "Hotel") {

                }
            } catch (e: Exception) {
                Log.e(TAG, "Transaction confirm failed", e)
                Toast.makeText(context, "Confirm failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun findCheckingCardByCustomerCode(
        customerCode: String,
        billType: String
    ): Boolean {
//        viewModelScope.launch {
        val snapshot = db.collection("bills").whereEqualTo("billType", billType)
            .whereEqualTo("customerCode", customerCode)
            .whereEqualTo("status", false)
            .get().await()
        if (snapshot.isEmpty) {
            return false
        }
        for (document in snapshot.documents) {
            val cardNumber = document.getString("cardNumber")
            val amount = document.getLong("amount") ?: 0L
            if (cardNumber != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        currentPayBill = Bill(
                            provider = document.getString("provider") ?: "",
                            amount = BigDecimal.valueOf(amount),
                            cardNumber = cardNumber,
                            description = document.getString("description") ?: "",
                        )
                    )
                }
            }
        }
        return true
//                .addOnSuccessListener { documents ->
//                    if (documents.isEmpty) {
//                        Log.d(TAG, "No matching documents")
//                    } else {
//                        for (document in documents) {
//                            val cardNumber = document.getString("cardNumber")
//                            val amount = document.getLong("amount") ?: 0L
//                            if (cardNumber != null) {
//                                _uiState.update { currentState ->
//                                    currentState.copy(
//                                        currentTransaction = TransactionRecord(
//                                            amount = BigDecimal.valueOf(amount),
//                                            destinationCard = cardNumber,
//                                        )
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d(TAG, "Error getting documents: ", exception)
//                }
//        }
    }

    fun createSavingAccount(context: Context) {
        viewModelScope.launch {
            val newCardNumber = generateUniqueCardNumber()
            addDocumentToCollection(
                collectionName = "saving",
                data = mapOf(
                    "accountId" to uiState.value.account.accountId,
                    "balance" to 0,
                    "cardNumber" to newCardNumber,
                    "profit" to 0,
                    "status" to "Inactive",
                    "withdrawDate" to 0
                ),
                documentId = newCardNumber,
                onSuccess = {
                    Toast.makeText(context, "Created saving card successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
    }

    fun createMortgageAccount(context: Context) {
        viewModelScope.launch {
            val newCardNumber = generateUniqueCardNumber()
            addDocumentToCollection(
                collectionName = "mortgage",
                data = mapOf(
                    "accountId" to uiState.value.account.accountId,
                    "loan" to 0,
                    "cardNumber" to newCardNumber,
                    "monthPayment" to 0,
                    "twoWeeksPayment" to 0,
                    "loanDate" to 0,
                    "payDate" to 0,
                    "status" to "Inactive"
                ),
                documentId = newCardNumber,
                onSuccess = {
                    Toast.makeText(
                        context,
                        "Created mortgage card successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        context,
                        "Created mortgage card successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    //deposit
    fun onStartDeposit(
        amount: BigDecimal, navController: NavHostController
    ) {
        viewModelScope.launch {


            _uiState.update { currentState ->
                currentState.copy(
                    currentTransaction = TransactionRecord(
                        transactionId = generateUniqueTransactionId(),
                        amount = amount,
                        fee = BigDecimal.ZERO,
                        timestamp = System.currentTimeMillis(),
                        sourceCard = "",
                        destinationCard = uiState.value.checkingCardNumber,
                        type = Service.Deposit.name
                    )
                )
            }
            navController.navigate(AppScreen.Transaction.name)
        }
    }

    fun onConfirmDeposit(
        cardNumber: String,
        bank: String,
        ownerName: String, context: Context,
        navController: NavHostController
    ) {

        val transaction = uiState.value.currentTransaction?.copy(
            sourceCard = cardNumber
        )
        if (transaction == null) {
            Log.e(TAG, "Transaction is null")
            Toast.makeText(context, "Transaction is null", Toast.LENGTH_SHORT).show()
            return
        }
        addDocumentToCollection(
            documentId = transaction.transactionId,
            collectionName = "transactionHistories",
            data = mapOf(
                "amount" to transaction.amount.toLong(),
                "fee" to transaction.fee.toLong(),
                "timestamp" to transaction.timestamp,
                "sourceCard" to transaction.sourceCard,
                "destinationCard" to transaction.destinationCard,
                "type" to transaction.type
            ),
            onSuccess = {
                addDocumentToCollection(
                    documentId = transaction.transactionId,
                    collectionName = "depositDetails",
                    data = mapOf(
                        "transactionID" to transaction.transactionId,
                        "fromCardNumber" to cardNumber,
                        "fromUserName" to ownerName,
                        "fromBank" to bank
                    ),
                    onSuccess = {
                        viewModelScope.launch {
                            val checkingCardNumber = uiState.value.checkingCardNumber
                            val checkingDocRef =
                                db.collection("checking").document(checkingCardNumber)
                            try {
                                val snapshot = checkingDocRef.get().await()
                                val currentBalance = snapshot.getLong("balance") ?: 0
                                val newBalance = currentBalance.plus(transaction.amount.toLong())
                                updateFieldInDocument(
                                    collectionName = "checking",
                                    documentId = checkingCardNumber,
                                    fieldName = "balance",
                                    newValue = newBalance
                                )
                                _uiState.update { currentUiState ->
                                    currentUiState.copy(
                                        currentTransaction = null
                                    )
                                }
                                Toast.makeText(context, "Deposit successful!", Toast.LENGTH_SHORT)
                                    .show()
                                navController.navigate(AppScreen.CustomerHome.name) {
                                    popUpTo(AppScreen.CustomerHome.name) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                Log.e("Deposit", "Failed to update balance: ${e.message}")
                            }
                        }

                    }
                )
            }
        )
    }

    fun onStartWithdraw(
        amount: BigDecimal, navController: NavHostController
    ) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    currentTransaction = TransactionRecord(
                        transactionId = generateUniqueTransactionId(),
                        amount = amount,
                        fee = BigDecimal.ZERO,
                        timestamp = System.currentTimeMillis(),
                        sourceCard = uiState.value.checkingCardNumber,
                        destinationCard = "",
                        type = Service.Withdraw.name
                    )
                )
            }
            navController.navigate(AppScreen.Transaction.name)
        }
    }

    fun onConfirmWithdraw(
        cardNumber: String,
        bank: String,
        ownerName: String, context: Context,
        navController: NavHostController
    ) {
        val transaction = uiState.value.currentTransaction?.copy(
            destinationCard = cardNumber
        )
        if (transaction == null) {
            Log.e(TAG, "Transaction is null")
            Toast.makeText(context, "Transaction is null", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            val checkingCardNumber = uiState.value.checkingCardNumber
            val checkingDocRef =
                db.collection("checking").document(checkingCardNumber)
            try {
                val snapshot = checkingDocRef.get().await()
                val currentBalance = snapshot.getLong("balance") ?: 0
                val newBalance = currentBalance.minus(transaction.amount.toLong())
                updateFieldInDocument(
                    collectionName = "checking",
                    documentId = checkingCardNumber,
                    fieldName = "balance",
                    newValue = newBalance
                )
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        currentTransaction = null
                    )
                }

            } catch (e: Exception) {
                Log.e("Deposit", "Failed to update balance: ${e.message}")
            }
        }

        addDocumentToCollection(
            documentId = transaction.transactionId,
            collectionName = "transactionHistories",
            data = mapOf(
                "amount" to transaction.amount.toLong(),
                "fee" to transaction.fee.toLong(),
                "timestamp" to transaction.timestamp,
                "sourceCard" to transaction.sourceCard,
                "destinationCard" to transaction.destinationCard,
                "type" to transaction.type
            ),
            onSuccess = {
                addDocumentToCollection(
                    documentId = transaction.transactionId,
                    collectionName = "withdrawDetails",
                    data = mapOf(
                        "transactionID" to transaction.transactionId,
                        "toCardNumber" to cardNumber,
                        "toUserName" to ownerName,
                        "toBank" to bank
                    ),
                    onSuccess = {
                        Toast.makeText(context, "Withdraw successful!", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(AppScreen.CustomerHome.name) {
                            popUpTo(AppScreen.CustomerHome.name) { inclusive = true }
                        }
                    }
                )
            }
        )
    }


    // Saving
    fun onSavingClick(
        cardNumber: String,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val isSavingActive =
                getFieldValueFromDocument("saving", cardNumber, "status") == "Active"

            if (isSavingActive) {
                // TODO: SHOW DIALOG
            } else {
                navController.navigate(AppScreen.Saving.name)
            }
        }
    }

    fun onConfirmSavingClick(
        term: Int,
        amount: BigDecimal,
        password: String,
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val email = uiState.value.account.email
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                transferBetweenCard(
                    sourceCard = uiState.value.checkingCardNumber,
                    destinationCard = uiState.value.savingCardNumber,
                    amount = amount
                )
                updateFieldInDocument(
                    collectionName = "saving",
                    documentId = uiState.value.savingCardNumber,
                    fieldName = "status",
                    newValue = "Active"
                )
                val withdrawDate = getMillisAfterMonths(term)
                updateFieldInDocument(
                    collectionName = "saving",
                    documentId = uiState.value.savingCardNumber,
                    fieldName = "withdrawDate",
                    newValue = withdrawDate
                )

                val transactionId = generateUniqueTransactionId()
                val timestamp = System.currentTimeMillis()
                val history = mapOf(
                    "amount" to amount.toDouble(),
                    "fee" to 0,
                    "sourceCard" to uiState.value.checkingCardNumber,
                    "destinationCard" to uiState.value.savingCardNumber,
                    "timestamp" to timestamp,
                    "type" to "Saving"
                )
                val detail = mapOf(
                    "transactionId" to transactionId,
                    "amount" to amount.toDouble(),
                    "fee" to 0,
                    "timestamp" to timestamp,
                    "sourceCard" to uiState.value.checkingCardNumber,
                    "destinationCard" to uiState.value.savingCardNumber,
                    "type" to "Saving",
                    "content" to "Transfer to saving",
                    "category" to "Saving"
                )

                addDocumentToCollection(
                    collectionName = "transferDetails",
                    data = detail,
                    documentId = transactionId,
                    onSuccess = {}
                )
                addDocumentToCollection(
                    collectionName = "transactionHistories",
                    data = history,
                    documentId = transactionId,
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Transaction successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack()
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Transaction confirm failed", e)
                Toast.makeText(
                    context,
                    "Confirm failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}