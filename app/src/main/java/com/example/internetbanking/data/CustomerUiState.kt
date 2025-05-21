package com.example.internetbanking.data

import java.math.BigDecimal
import java.util.UUID

data class CustomerUiState(
    val currentViewType: String = "Checking",
    val currentCardView: String = "",

    // All Customer
    val account: User = User(), // accountId, fullName, gender, identificationNumber, phoneNumber, email, birthday, address, role
    val transactionHistory: List<TransactionRecord> = emptyList(),
    val transactionHistoryToView: TransactionRecord = TransactionRecord(),


    // Checking
    val checkingCardNumber: String = "",
    val checkingBalance: BigDecimal = BigDecimal.ZERO,
    val checkingCurrentTransfer: TransactionDetail = TransactionDetail(),

    // Saving
    val savingCardNumber: String = "",
    val savingBalance: BigDecimal = BigDecimal.ZERO,
    val savingProfits: BigDecimal = BigDecimal.ZERO,
    val savingWithdrawDate: Long = 0L,
    val savingStatus: String = "",

    // Mortgage
    val mortgageCardNumber: String = "",
    val mortgageLoan: BigDecimal = BigDecimal.ZERO,
    val mortgageMonthPayment: BigDecimal = BigDecimal.ZERO,
    val mortgage2WeeksPayment: BigDecimal = BigDecimal.ZERO,
    val mortgageLoadDate: Long = 0L,
    val mortgagePayDate: Long = 0L,
    val mortgageStatus: String = "",

    //deposit
    val currentTransaction: TransactionRecord? = null,

    )

data class TransactionRecord(
    val transactionId: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val fee: BigDecimal = BigDecimal.ZERO,
    val timestamp: Long = 0L,
    val sourceCard: String = "",
    val destinationCard: String = "",
    val type: String = ""
)

data class TransactionDetail(
    val transaction: TransactionRecord = TransactionRecord(),
    val content: String = "",
    val category: String = ""
)
data class Deposit(
    val transactionID :String  ="",
    val fromCardNumber:String ="",
    val fromUserName: String= "",
    val fromBank: String = "",
)