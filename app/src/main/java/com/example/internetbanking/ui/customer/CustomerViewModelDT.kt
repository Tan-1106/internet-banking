package com.example.internetbanking.ui.customer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.internetbanking.data.CustomerUiStateDT
import com.example.internetbanking.data.DepositDT
import com.example.internetbanking.data.TransactionDT
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.util.UUID

class CustomerViewModelDT : ViewModel() {
    private val _uiState = MutableStateFlow(CustomerUiStateDT())
    val uiState = _uiState.asStateFlow()
    val db = FirebaseFirestore.getInstance()
    var depositAmount: BigDecimal by mutableStateOf(BigDecimal.ZERO)
    fun onDepositClick(
        cardNumber: String,
        bank: String,
        ownerName: String
    ) {

        val transaction = TransactionDT(
            id = UUID.randomUUID().toString(),
            cardNumber = "2817361",
            fee = 0,
            time = System.currentTimeMillis(),
            amount = depositAmount.toLong(),
            type = "Deposit",
        )
        val depositdetail = DepositDT(
            transactionID = transaction.id,
            fromCardNumber = cardNumber,
            fromUserName = ownerName,
            fromBank = bank
        )
        db.collection("transactionDT")
            .document(transaction.id)
            .set(transaction)
            .addOnSuccessListener {
                db.collection("depositDetailDT")
                    .document(transaction.id)
                    .set(depositdetail)
                    .addOnSuccessListener {
                        Log.i("created", "Transaction created successfully")
                    }
            }
    }
}
