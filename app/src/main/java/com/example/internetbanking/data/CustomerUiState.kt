package com.example.internetbanking.data

import java.math.BigDecimal

data class CustomerUiState(
    // All Customer
    val account: User = User(),
    val cardNumber: String = "",
    val transactionHistory: List<TransactionRecord> = emptyList(),

    // Checking and Saving
    val balance: BigDecimal = BigDecimal.ZERO,

    // Saving Only
    val profitsForSavingAccount: BigDecimal = BigDecimal.ZERO,

    // Mortgage Only
    val mortgageInformation: MortgageInformation = MortgageInformation()
)

data class TransactionRecord(
    val id: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val timestamp: Long = 0L
)

data class MortgageInformation(
    val paymentPerMonth: BigDecimal = BigDecimal.ZERO,
    val paymentPer2Weeks: BigDecimal = BigDecimal.ZERO
)