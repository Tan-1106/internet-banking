package com.example.internetbanking.data

import java.math.BigDecimal

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
    val mortgageLoanDate: Long = 0L,
    val mortgagePayDate: Long = 0L,
    val mortgageStatus: String = "",

    //deposit
    val currentTransaction: TransactionRecord? = null,
    //paybill
    val currentPayBill: Bill? = null
)

data class TransactionRecord(
    val transactionId: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val fee: BigDecimal = BigDecimal.ZERO,
    val timestamp: Long = 0L,
    val sourceCard: String = "",
    val destinationCard: String = "",
    val type: String = "",

    // Movie $ Flight
    val startTime: String = "",
    val seats: List<String> = emptyList(),

    // Phone TopUp
    val destinationPhoneNumber: String = "",
    val network: String = "",

    // Pay Bill
    val billType: String = "",
    val customerCode: String = "",
    val provider: String = "",

    // Flight
    val flightProvider: String = "",
    val numberOfPassengers: Int = 0,

    // Movie
    val movieName: String = "",
    val cinema: String = "",

    // Hotel
    val hotelName: String = "",
    val room: String = ""
)

data class TransactionDetail(
    val transaction: TransactionRecord = TransactionRecord(),
    val content: String = "",
    val category: String = ""
)

data class Bill(
    val transactionID: String = "",
    val billType: String = "",
    val customerCode: String = "",
    val provider: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val fee: BigDecimal = BigDecimal.ZERO,
    val cardNumber: String = "",
    val description: String = "",
    val status: Boolean = false,
)

data class Deposit(
    val transactionID: String = "",
    val fromCardNumber: String = "",
    val fromUserName: String = "",
    val fromBank: String = "",
)