package com.example.internetbanking.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.internetbanking.data.OfficerUiState
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal

class OfficerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OfficerUiState())
    val uiState: StateFlow<OfficerUiState> = _uiState.asStateFlow()

    private val db: FirebaseFirestore = Firebase.firestore

    fun loadLatestRates() {
        viewModelScope.launch {
            val snapshot = db.collection("profitRates")
                .orderBy("ratesChangeTimestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                val profitableRatesStr = doc.getString("profitableRates") ?: "0"
                val timestampStr = doc.getString("ratesChangeTimestamp") ?: "0"

                _uiState.update { currentState ->
                    currentState.copy(
                        profitableRates = profitableRatesStr.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        ratesChangeTimestamp = timestampStr.toLong()
                    )
                }
            }
        }
    }

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

    // Change Profitable Rates


    fun onValidateNewRateInput(newRate: String, context: Context): Boolean {
        if(newRate.isEmpty()) {
            Toast.makeText(context, "Enter new profitable rates to change", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun onChangeRatesConfirm(newRate: String) {
        val newRateData = mapOf(
            "profitableRates" to newRate,
            "ratesChangeTimestamp" to System.currentTimeMillis().toString()
        )

        db.collection("profitRates")
            .add(newRateData)
            .addOnSuccessListener {
                Log.d("Rates", "Successfully added new rate")
            }
            .addOnFailureListener { e ->
                Log.e("Rates", "Error adding new rate", e)
            }

        loadLatestRates()
    }

    // Edit Customer
    fun onSearchClick(cardNumber: String, context: Context, navController: NavHostController) {
        if (cardNumber.isEmpty()) {
            Toast.makeText(context, "Enter card number to search", Toast.LENGTH_SHORT).show()
        }
        // TODO: VERIFY CARD NUMBER AND NAVIGATE TO EDIT
    }
}