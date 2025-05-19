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
import com.example.internetbanking.AppScreen
import com.example.internetbanking.data.OfficerUiState
import com.example.internetbanking.data.User
import com.example.internetbanking.ui.shared.addDocumentToCollection
import com.example.internetbanking.ui.shared.checkExistData
import com.example.internetbanking.ui.shared.updateUserFieldByAccountId
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
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

    // Fetch Newest Profitable Rate
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

    // Error Messages
    var accountIdErrorMessage by mutableStateOf("")
        private set
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
        accountIdErrorMessage = ""
        nameErrorMessage = ""
        genderErrorMessage = ""
        idErrorMessage = ""
        phoneNumberErrorMessage = ""
        emailErrorMessage = ""
        birthdayErrorMessage = ""
        addressErrorMessage = ""
        roleErrorMessage = ""
    }



    // Validate Input Field
    suspend fun validateInputs(
        accountId: String,
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

        // Account ID
        if (accountId.isEmpty()) {
            accountIdErrorMessage = "User ID is required"
            isValid = false
        } else if (accountId.contains(Regex("\\s"))) {
            accountIdErrorMessage = "User ID cannot have spaces"
            isValid = false
        } else if (checkExistData("users", "accountId", accountId)) {
            accountIdErrorMessage = "This account ID already exists"
            isValid = false
        } else {
            accountIdErrorMessage = ""
        }

        // Full name
        if (fullName.isEmpty()) {
            nameErrorMessage = "Full name is required"
            isValid = false
        } else {
            nameErrorMessage = ""
        }

        // Gender
        if (gender.isEmpty()) {
            genderErrorMessage = "Gender is required"
            isValid = false
        } else {
            genderErrorMessage = ""
        }

        // ID number
        if (idNumber.isEmpty()) {
            idErrorMessage = "Identification number is required"
            isValid = false
        } else if (idNumber.length != 9 && idNumber.length != 12) {
            idErrorMessage = "Invalid identification number"
            isValid = false
        } else {
            idErrorMessage = ""
        }

        // Phone number
        if (phoneNumber.isEmpty()) {
            phoneNumberErrorMessage = "Phone number is required"
            isValid = false
        } else if (phoneNumber.length != 10 && phoneNumber.length != 11) {
            phoneNumberErrorMessage = "Invalid phone number"
            isValid = false
        } else {
            phoneNumberErrorMessage = ""
        }

        // Email
        if (email.isEmpty()) {
            emailErrorMessage = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            emailErrorMessage = "Invalid email format"
            isValid = false
        } else if (checkExistData("users", "email", email.trim())) {
            emailErrorMessage = "This email already exists"
            isValid = false
        } else {
            emailErrorMessage = ""
        }

        // Birthday
        if (birthday.isEmpty()) {
            birthdayErrorMessage = "Birthday is required"
            isValid = false
        } else {
            birthdayErrorMessage = ""
        }

        // Address
        if (address.isEmpty()) {
            addressErrorMessage = "Address is required"
            isValid = false
        } else {
            addressErrorMessage = ""
        }

        // Role
        if (role.isEmpty()) {
            roleErrorMessage = "Role is required"
            isValid = false
        } else {
            roleErrorMessage = ""
        }

        return isValid
    }


    fun createUserAndSendResetEmail(
        email: String,
        tempPassword: String = "Temp@1234",
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, tempPassword)
            .addOnSuccessListener {
                // Gửi email đặt lại mật khẩu
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Create Customer
    fun onCreateCustomerButtonClick(
        context: Context,
        navController: NavHostController,
        accountId: String,
        fullName: String,
        gender: String,
        idNumber: String,
        phoneNumber: String,
        email: String,
        birthday: String,
        address: String,
        role: String
    ) {
        viewModelScope.launch {
            val isValid = validateInputs(
                accountId, fullName, gender, idNumber,
                phoneNumber, email, birthday, address, role
            )

            if (isValid) {
                val customerData = mapOf(
                    "accountId" to accountId,
                    "fullName" to fullName,
                    "gender" to gender,
                    "identificationNumber" to idNumber,
                    "phoneNumber" to phoneNumber,
                    "email" to email,
                    "birthday" to birthday,
                    "address" to address,
                    "role" to role
                )

                addDocumentToCollection(
                    collectionName = "users",
                    data = customerData,
                    documentId = accountId,
                    onSuccess = {
                        Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show()
                    }
                )

                createUserAndSendResetEmail(
                    email = email,
                    onSuccess = {
                        Log.d("CreateUser", "User created and email sent!")
                    },
                    onFailure = { e ->
                        Log.e("CreateUser", "Error: ${e.message}")
                    }
                )

                navController.navigateUp()
            }
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
    fun onSearchClick(accountId: String, context: Context, navController: NavHostController) {
        if (accountId.isEmpty()) {
            Toast.makeText(context, "Enter account ID to search", Toast.LENGTH_SHORT).show()
        } else {
            viewModelScope.launch {
                try {
                    val snapshot = db.collection("users").document(accountId).get().await()

                    if (!snapshot.exists()) {
                        Toast.makeText(context, "No user found with this account ID", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val customer = User(
                        accountId = snapshot.getString("accountId") ?: "",
                        fullName = snapshot.getString("fullName") ?: "",
                        gender = snapshot.getString("gender") ?: "",
                        identificationNumber = snapshot.getString("identificationNumber") ?: "",
                        phoneNumber = snapshot.getString("phoneNumber") ?: "",
                        email = snapshot.getString("email") ?: "",
                        birthday = snapshot.getString("birthday") ?: "",
                        address = snapshot.getString("address") ?: "",
                        role = snapshot.getString("role") ?: ""
                    )

                    _uiState.update { it.copy(customerToEdit = customer) }

                    // Navigate to edit screen (replace with your actual route)
                    navController.navigate(AppScreen.EditCustomerProfile.name)

                } catch (e: Exception) {
                    Toast.makeText(context, "Error fetching user: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("onSearchClick", "Firestore error", e)
                }
            }
        }
    }

    fun validateEditInput(
        fullName: String,
        gender: String,
        phoneNumber: String,
        birthday: String,
        address: String
    ): Boolean {
        var isValid = true

        // Full name
        if (fullName.isEmpty()) {
            nameErrorMessage = "Full name is required"
            isValid = false
        } else {
            nameErrorMessage = ""
        }

        // Gender
        if (gender.isEmpty()) {
            genderErrorMessage = "Gender is required"
            isValid = false
        } else {
            genderErrorMessage = ""
        }

        // Phone number
        if (phoneNumber.isEmpty()) {
            phoneNumberErrorMessage = "Phone number is required"
            isValid = false
        } else if (phoneNumber.length != 10 && phoneNumber.length != 11) {
            phoneNumberErrorMessage = "Invalid phone number"
            isValid = false
        } else {
            phoneNumberErrorMessage = ""
        }

        // Birthday
        if (birthday.isEmpty()) {
            birthdayErrorMessage = "Birthday is required"
            isValid = false
        } else {
            birthdayErrorMessage = ""
        }

        // Address
        if (address.isEmpty()) {
            addressErrorMessage = "Address is required"
            isValid = false
        } else {
            addressErrorMessage = ""
        }

        return isValid
    }
    fun onCustomerEditClick(
        context: Context,
        navController: NavHostController,
        fullName: String,
        gender: String,
        phoneNumber: String,
        birthday: String,
        address: String
    ) {
        if (validateEditInput(fullName, gender, phoneNumber, birthday, address)) {
            updateUserFieldByAccountId(
                accountId = uiState.value.customerToEdit.accountId,
                fieldName = "fullName",
                newValue = fullName,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            updateUserFieldByAccountId(
                accountId = uiState.value.customerToEdit.accountId,
                fieldName = "gender",
                newValue = gender,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            updateUserFieldByAccountId(
                accountId = uiState.value.customerToEdit.accountId,
                fieldName = "phoneNumber",
                newValue = phoneNumber,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            updateUserFieldByAccountId(
                accountId = uiState.value.customerToEdit.accountId,
                fieldName = "birthday",
                newValue = birthday,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )
            updateUserFieldByAccountId(
                accountId = uiState.value.customerToEdit.accountId,
                fieldName = "address",
                newValue = address,
                onSuccess = {},
                onFailure = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
            )

            Toast.makeText(context, "Information updated successfully", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }
}