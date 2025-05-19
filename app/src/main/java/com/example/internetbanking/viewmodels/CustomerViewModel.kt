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
import kotlinx.coroutines.tasks.await
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

                // Cập nhật UI State
                _uiState.update { it.copy(balance = balance) }
            }
        }
    }

    // Load Customer Information
    var hasSaving by mutableStateOf(false)
        private set
    var hasMortgage by mutableStateOf(false)
        private set

    fun loadCustomerInformation(currentUser: User) {
        val role = currentUser.role
        val accountId = currentUser.accountId
        viewModelScope.launch {
            val cardNumber = if (role == "Checking") {
                getFieldFromDocument("users", accountId, "checking.cardNumber")
            } else if (role == "Saving") {
                getFieldFromDocument("users", accountId, "saving.cardNumber")
            } else {
                getFieldFromDocument("users", accountId, "mortgage.cardNumber")
            }

            _uiState.update { currentState ->
                currentState.copy(
                    account = currentUser,
                    cardNumber = cardNumber.toString()
                )
            }
            observeBalance(accountId, role)
            observeSubAccountsExistence(accountId)
        }
    }

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
}