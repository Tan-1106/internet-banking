package com.example.internetbanking.data

data class LoginUiState(
    val currentUser: User = User(),
    val loginFailedMessage: String = ""
)

data class User(
    val userId: String = "",
    val fullName: String = "",
    val gender: String = "",
    val identificationNumber: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val birthday: String = "",
    val address: String = "",
    val role: String = ""
)

data class Customer(
    val cardNumber: String = "",
    val account: User = User()
)