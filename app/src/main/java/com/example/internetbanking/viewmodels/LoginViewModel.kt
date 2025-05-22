package com.example.internetbanking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.internetbanking.AppScreen
import com.example.internetbanking.data.LoginUiState
import com.example.internetbanking.data.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    fun login(
        accountId: String,
        password: String,
        navController: NavHostController
    ) {
        if (accountId.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(loginFailedMessage = "Please enter account ID and password") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginFailedMessage = "") }
            try {
                val document = db.collection("users").document(accountId).get().await()
                if (!document.exists()) {
                    _uiState.update { it.copy(isLoading = false, loginFailedMessage = "User's ID doesn't exist") }
                    return@launch
                }
                val email = document.getString("email")
                if (email == null) {
                    _uiState.update { it.copy(isLoading = false, loginFailedMessage = "Can't find the email of this user") }
                    return@launch
                }

                // Success login
                auth.signInWithEmailAndPassword(email, password).await()

                val accountId = document.getString("accountId") ?: ""
                val fullName = document.getString("fullName") ?: ""
                val gender = document.getString("gender") ?: ""
                val idNumber = document.getString("identificationNumber") ?: ""
                val phoneNumber = document.getString("phoneNumber") ?: ""
                val birthday = document.getString("birthday") ?: ""
                val address = document.getString("address") ?: ""
                val role = document.getString("role") ?: ""

                val currentUser = User(
                    accountId = accountId,
                    fullName = fullName,
                    gender = gender,
                    identificationNumber = idNumber,
                    phoneNumber = phoneNumber,
                    email = email,
                    birthday = birthday,
                    address = address,
                    role = role
                )

                _uiState.update {
                    it.copy(
                        currentUser =  currentUser,
                        isLoading = false,
                        isLoggedIn = true,
                        loginFailedMessage = ""
                    )
                }

                when (role) {
                    "Officer" -> navController.navigate(AppScreen.OfficerHome.name){
                        popUpTo(AppScreen.Login.name){
                            inclusive = true
                        }
                        launchSingleTop = true

                    }
                    else -> navController.navigate(AppScreen.CustomerHome.name){
                        popUpTo(AppScreen.Login.name){
                            inclusive = true
                        }
                        launchSingleTop = true

                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginFailedMessage = "Login failed: ${e.message}"
                    )
                }
            }
        }
    }

    fun logout(navController: NavHostController) {
        FirebaseAuth.getInstance().signOut()

        _uiState.update {
            it.copy(
                isLoggedIn = false,
                currentUser = User()
            )
        }

        navController.navigate(AppScreen.Login.name) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun updateCurrentCustomerInformation(
        fullName: String,
        gender: String,
        phoneNumber: String,
        birthday: String,
        address: String
    ) {
        _uiState.update { currentState ->
            val currentAccount = currentState.currentUser
            currentState.copy(
                currentUser = User(
                    accountId = currentAccount.accountId,
                    fullName = fullName,
                    gender = gender,
                    identificationNumber = currentAccount.identificationNumber,
                    phoneNumber = phoneNumber,
                    email = currentAccount.email,
                    birthday = birthday,
                    address = address,
                    role = currentAccount.role
                )
            )
        }
    }
}