package com.example.internetbanking.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internetbanking.data.CustomerUiState
import com.example.internetbanking.data.User
import com.example.internetbanking.ui.shared.getFieldFromDocument
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

    // Real Time Balance
    fun observeBalance(accountId: String, role: String) {
        val db = FirebaseFirestore.getInstance()
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
        }
    }
}