package com.example.internetbanking.ui.officer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.data.Customer
import com.example.internetbanking.ui.shared.DatePicker
import com.example.internetbanking.ui.shared.GreenGradientButton
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.OfficerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCustomerProfile(
    officerViewModel: OfficerViewModel,
    navController: NavHostController
) {
    val officerUiState by officerViewModel.uiState.collectAsState()
    val customer: Customer = officerUiState.customerToEdit

    var fullName by remember { mutableStateOf(customer.account.fullName) }
    var gender by remember { mutableStateOf(customer.account.gender) }
    var identificationNumber by remember { mutableStateOf(customer.account.gender) }
    var phoneNumber by remember { mutableStateOf(customer.account.phoneNumber) }
    var email by remember { mutableStateOf(customer.account.email) }
    var birthday by remember { mutableStateOf(customer.account.birthday) }
    var address by remember { mutableStateOf(customer.account.address) }
    var role by remember { mutableStateOf(customer.account.role) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.sub_background1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Edit customer",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = custom_mint_green
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                                officerViewModel.clearErrorMessage()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Navigate Back",
                                tint = custom_mint_green,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .background(
                            brush = GradientColors.Green_DarkToLight
                        )
                )
            },
            modifier = Modifier.systemBarsPadding()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text(
                    "Edit Customer Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = custom_dark_green,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                LazyColumn {
                    item {
                        InformationLine(
                            label = "Full name",
                            placeholder = "Enter full name",
                            value = fullName,
                            onValueChange = { fullName = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            errorMessage = officerViewModel.nameErrorMessage
                        )
                    }
                    item{
                        InformationSelect(
                            label = "Gender",
                            placeholder = "Select gender",
                            options = listOf("Male", "Female"),
                            onOptionSelected = { gender = it },
                            suffix = {
                                VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select gender")
                            },
                            errorMessage = officerViewModel.genderErrorMessage
                        )
                    }
                    item {
                        InformationLine(
                            label = "Identification number",
                            placeholder = "Enter ID",
                            value = identificationNumber,
                            onValueChange = { identificationNumber = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            errorMessage = officerViewModel.idErrorMessage
                        )
                    }
                    item {
                        InformationLine(
                            label = "Phone number",
                            placeholder = "Enter phone number",
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            errorMessage = officerViewModel.phoneNumberErrorMessage
                        )
                    }
                    item {
                        InformationLine(
                            label = "Email",
                            placeholder = "Enter email",
                            value = email,
                            onValueChange = { email = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            errorMessage = officerViewModel.emailErrorMessage
                        )
                    }
                    item {
                        DatePicker(
                            label = "Birthday",
                            placeholder = "Select birthday",
                            onDatePick = { birthday = it },
                            suffix = {
                                VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                                Icon(Icons.Filled.DateRange, contentDescription = "Select birthday")
                            },
                            errorMessage = officerViewModel.birthdayErrorMessage
                        )
                    }
                    item {
                        InformationLine(
                            label = "Address",
                            placeholder = "Enter address",
                            value = address,
                            onValueChange = { address = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            errorMessage = officerViewModel.addressErrorMessage
                        )
                    }
                    item {
                        InformationSelect(
                            label = "Role",
                            placeholder = role,
                            options = listOf("Checking", "Saving", "Mortgage"),
                            onOptionSelected = { role = it },
                            suffix = {
                                VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select role")
                            },
                            errorMessage = officerViewModel.roleErrorMessage
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item {
                        GreenGradientButton(
                            onButtonClick = {
                                // TODO: EDIT EVENT
                            },
                            buttonText = "Save",
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun EditCustomerProfilePreview(){
    val fakeViewModel: OfficerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    EditCustomerProfile(fakeViewModel, fakeNavController)
}