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
import com.example.internetbanking.data.CustomerUiState
import com.example.internetbanking.data.TransactionRecord
import com.example.internetbanking.data.User
import com.example.internetbanking.ui.shared.getFieldFromDocument
import com.example.internetbanking.ui.shared.updateUserFieldByAccountId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CustomerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    // Real Time Balance
    fun observeBalance(accountId: String, role: String) {
        val path = when (role) {
            "Checking" -> "checking.balance"
            "Saving" -> "saving.balance"
            else -> return
        }

        val documentRef = db.collection("users").document(accountId)

        documentRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("BalanceObserver", "Listen failed: ${error.message}")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val balanceValue = snapshot.get(path)
                val balance = when (balanceValue) {
                    is Number -> BigDecimal.valueOf(balanceValue.toDouble())
                    is String -> balanceValue.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    else -> BigDecimal.ZERO
                }

                // UI State
                if (role == "Checking") {
                    _uiState.update { it.copy(checkingBalance = balance) }
                } else if (role == "Saving") {
                    _uiState.update { it.copy(savingBalance = balance) }
                }
            }
        }
    }

    // Observe Exist Types
    fun observeSubAccountsExistence(accountId: String) {
        val docRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(accountId)

        docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                hasSaving = snapshot.contains("saving")
                hasMortgage = snapshot.contains("mortgage")
            }
        }
    }

    // Load Customer Information
    var hasSaving by mutableStateOf(false)
        private set
    var hasMortgage by mutableStateOf(false)
        private set
    fun loadCustomerInformation(currentUser: User) {
        val viewType = uiState.value.currentViewType
        val accountId = currentUser.accountId
        viewModelScope.launch {
            if (viewType == "Checking") {
                _uiState.update { currentState ->
                    currentState.copy(
                        account = currentUser,
                        checkingCardNumber = getFieldFromDocument("users", accountId, "checking.cardNumber").toString()
                    )
                }
            } else if (viewType == "Saving") {
                _uiState.update { currentState ->
                    currentState.copy(
                        account = currentUser,
                        savingCardNumber = getFieldFromDocument("users", accountId, "saving.cardNumber").toString()
                    )
                }
            } else if (viewType == "Mortgage") {
                _uiState.update { currentState ->
                    currentState.copy(
                        account = currentUser,
                        mortgageCardNumber = getFieldFromDocument("users", accountId, "mortgage.cardNumber").toString()
                    )
                }
            }
            observeBalance(accountId, viewType)
            observeSubAccountsExistence(accountId)
            observeTransactionHistory(accountId)
        }
    }

    // Edit Information
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

    fun onEditSaveClick(
        context: Context,
        navController: NavHostController,
        fullName: String,
        gender: String,
        phoneNumber: String,
        birthday: String,
        address: String,
        loginViewModel: LoginViewModel
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
}