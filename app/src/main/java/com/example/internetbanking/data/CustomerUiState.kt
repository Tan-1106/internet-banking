package com.example.internetbanking.data

import java.math.BigDecimal

data class CustomerUiState(
    val currentViewType: String = "Checking",

    // All Customer
    val account: User = User(),
    val transactionHistory: List<TransactionRecord> = emptyList(),

    // Checking
    val checkingCardNumber: String = "",
    val checkingBalance: BigDecimal = BigDecimal.ZERO,

    // Saving
    val savingCardNumber: String = "",
    val savingBalance: BigDecimal = BigDecimal.ZERO,
    val profitsForSavingAccount: BigDecimal = BigDecimal.ZERO,

    // Mortgage
    val mortgageCardNumber: String = "",
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