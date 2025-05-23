package com.example.internetbanking.ui.customer

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.internetbanking.ui.shared.DatePicker
import com.example.internetbanking.ui.shared.GreenGradientButton
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel
import com.example.internetbanking.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    customerViewModel: CustomerViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current
    val customerUiState by customerViewModel.uiState.collectAsState()

    var customerName by remember { mutableStateOf(customerUiState.account.fullName) }
    var customerGender by remember { mutableStateOf(customerUiState.account.gender) }
    var customerPhoneNumber by remember { mutableStateOf(customerUiState.account.phoneNumber) }
    var customerBirthday by remember { mutableStateOf(customerUiState.account.birthday) }
    var customerAddress by remember { mutableStateOf(customerUiState.account.address) }

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
                            text = "Your Information",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = custom_mint_green
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back",
                            tint = custom_mint_green,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    navController.navigateUp()
                                    customerViewModel.clearErrorMessage()
                                }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .background(
                            brush = GradientColors.Green_Ripple
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
                    "Your Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = custom_dark_green,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                LazyColumn {
                    item {
                        InformationLine(
                            label = "Account ID",
                            placeholder = customerUiState.account.accountId,
                            value = "",
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        InformationLine(
                            label = "Full name",
                            placeholder = "Enter full name",
                            value = customerName,
                            onValueChange = { customerName = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            suffix = {
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(0.8f),
                                    color = Color.Gray
                                )
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Information")
                            },
                            errorMessage = customerViewModel.nameErrorMessage
                        )
                    }
                    item {
                        InformationSelect(
                            label = "Gender",
                            placeholder = customerGender,
                            options = listOf("Male", "Female"),
                            onOptionSelected = { customerGender = it },
                            suffix = {
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(0.8f),
                                    color = Color.Gray
                                )
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = "Select gender"
                                )
                            },
                            errorMessage = customerViewModel.genderErrorMessage
                        )
                    }
                    item {
                        InformationLine(
                            label = "Identification number",
                            placeholder = customerUiState.account.identificationNumber,
                            value = "",
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        InformationLine(
                            label = "Phone number",
                            placeholder = "Enter phone number",
                            value = customerPhoneNumber,
                            onValueChange = { customerPhoneNumber = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            suffix = {
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(0.8f),
                                    color = Color.Gray
                                )
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Information")
                            },
                            errorMessage = customerViewModel.phoneNumberErrorMessage
                        )
                    }
                    item {
                        InformationLine(
                            label = "Email",
                            placeholder = customerUiState.account.email,
                            value = "",
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        DatePicker(
                            label = "Birthday",
                            placeholder = customerBirthday,
                            onDatePick = { customerBirthday = it },
                            suffix = {
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(0.8f),
                                    color = Color.Gray
                                )
                                Icon(Icons.Filled.DateRange, contentDescription = "Select birthday")
                            },
                            errorMessage = customerViewModel.birthdayErrorMessage
                        )
                    }
                    item {
                        InformationLine(
                            label = "Address",
                            placeholder = "Enter address",
                            value = customerAddress,
                            onValueChange = { customerAddress = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            suffix = {
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(0.8f),
                                    color = Color.Gray
                                )
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Information")
                            },
                            errorMessage = ""
                        )
                    }
                    item {
                        InformationLine(
                            label = "Role",
                            placeholder = customerUiState.account.role,
                            value = "",
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item {
                        GreenGradientButton(
                            onButtonClick = {
                                customerViewModel.onEditSaveClick(
                                    context = context,
                                    navController = navController,
                                    fullName = customerName,
                                    gender = customerGender,
                                    phoneNumber = customerPhoneNumber,
                                    birthday = customerBirthday,
                                    address = customerAddress,
                                    loginViewModel = loginViewModel
                                )
                            },

                            buttonCustom = {
                                Text(
                                    text = "Save",
                                    fontSize = 20.sp
                                )
                            },
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
fun ProfileScreenPreview() {
    val fakeCustomerViewModel: CustomerViewModel = viewModel()
    val fakeLoginViewModel: LoginViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    ProfileScreen(fakeCustomerViewModel, fakeLoginViewModel, fakeNavController)
}