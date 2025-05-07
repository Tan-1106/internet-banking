package com.example.internetbanking.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.internetbanking.data.OfficerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OfficerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OfficerUiState())
    val uiState: StateFlow<OfficerUiState> = _uiState.asStateFlow()

    // Create Customer
    var nameErrorMessage by mutableStateOf("")
        private set
    var genderErrorMessage by mutableStateOf("")
        private set
    var idErrorMessage by mutableStateOf("")
        private set
    var phoneNumberErrorMessage by mutableStateOf("")
        private set
    var emailErrorMessage by mutableStateOf("")
        private set
    var birthdayErrorMessage by mutableStateOf("")
        private set
    var addressErrorMessage by mutableStateOf("")
        private set
    var roleErrorMessage by mutableStateOf("")
        private set
    fun clearErrorMessage() {
        nameErrorMessage = ""
        genderErrorMessage = ""
        idErrorMessage = ""
        phoneNumberErrorMessage = ""
        emailErrorMessage = ""
        birthdayErrorMessage = ""
        addressErrorMessage = ""
        roleErrorMessage = ""
    }

    fun validateInputs(
        fullName: String,
        gender: String,
        idNumber: String,
        phoneNumber: String,
        email: String,
        birthday: String,
        address: String,
        role: String
    ): Boolean {
        var isValid = true

        if (fullName.isEmpty()) {
            nameErrorMessage = "Full name is required"
            isValid = false
        } else {
            nameErrorMessage = ""
        }

        if (gender.isEmpty()) {
            genderErrorMessage = "Gender is required"
            isValid = false
        } else {
            genderErrorMessage = ""
        }

        if (idNumber.isEmpty()) {
            idErrorMessage = "Identification number is required"
            isValid = false
        } else {
            idErrorMessage = ""
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberErrorMessage = "Phone number is required"
            isValid = false
        } else {
            phoneNumberErrorMessage = ""
        }

        if (email.isEmpty()) {
            emailErrorMessage = "Email is required"
            isValid = false
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            emailErrorMessage = "Invalid email format"
            isValid = false
        } else {
            emailErrorMessage = ""
        }

        if (birthday.isEmpty()) {
            birthdayErrorMessage = "Birthday is required"
            isValid = false
        } else {
            birthdayErrorMessage = ""
        }

        if (address.isEmpty()) {
            addressErrorMessage = "Address ís required"
            isValid = false
        } else {
            addressErrorMessage = ""
        }

        if (role.isEmpty()) {
            roleErrorMessage = "Role is required"
            isValid = false
        } else {
            roleErrorMessage = ""
        }

        return isValid
    }

    fun onCreateCustomerButtonClick(
        fullName: String,
        gender: String,
        idNumber: String,
        phoneNumber: String,
        email: String,
        birthday: String,
        address: String,
        role: String
    ) {
        if (validateInputs(fullName, gender, idNumber, phoneNumber, email, birthday, address, role)) {
            // TODO: CREATE CUSTOMER EVENT
        }
    }
}