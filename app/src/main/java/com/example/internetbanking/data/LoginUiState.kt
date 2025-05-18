package com.example.internetbanking.data

data class LoginUiState(
    val currentUser: User = User(),
    val loginFailedMessage: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)

data class User(
    val accountId: String = "",
    val fullName: String = "",
    val gender: String = "",
    val identificationNumber: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val birthday: String = "",
    val address: String = "",
    val role: String = ""
)