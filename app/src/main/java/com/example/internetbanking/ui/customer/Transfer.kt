package com.example.internetbanking.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.internetbanking.ui.shared.BalanceInformation
import com.example.internetbanking.ui.shared.GreenGradientButton
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.shared.checkCardNumberExistsInCollections
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val customerUiState by customerViewModel.uiState.collectAsState()

    val banks = listOf<String>("Vietcombank", "Sacombank", "Techcombank", "Agribank", "VietinBank")
    val categories = listOf<String>(
        "Supermarket", "Dining", "Billing payment",
        "House rental", "Traffic spending", "Home helper", "Others"
    )

    var beneficiaryBank by remember { mutableStateOf("") }
    var beneficiaryBankEM by remember { mutableStateOf("") }

    var beneficiaryAccount by remember { mutableStateOf("") }
    var beneficiaryAccountEM by remember { mutableStateOf("") }

    var amount by remember { mutableStateOf(BigDecimal.ZERO) }
    var amountEM by remember { mutableStateOf("") }

    var content by remember { mutableStateOf("${customerUiState.account.fullName} money transfer") }

    var category by remember { mutableStateOf("") }
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
                            text = stringResource(R.string.transfer_title),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = custom_mint_green
                        )
                    },
                    navigationIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back",
                            tint = custom_mint_green,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    navController.navigateUp()
                                }
                        )
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
        ) { paddingValue ->
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .padding(paddingValue)
                    .padding(vertical = 20.dp)
                    .fillMaxSize()

            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    item {
                        BalanceInformation(
                            cardNumber = customerUiState.checkingCardNumber,
                            balance = formatCurrencyVN(customerUiState.checkingBalance)
                        )
                    }
                    item {
                        Text(
                            "Beneficiary Information",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                    item {
                        InformationSelect(
                            label = "Beneficiary bank",
                            placeholder = "Select beneficiary bank",
                            options = banks,
                            onOptionSelected = { beneficiaryBank = it },
                            suffix = {
                                VerticalDivider(
                                    modifier = Modifier
                                        .fillMaxHeight(0.8f),
                                    color = Color.Gray
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Beneficiary Bank"
                                )
                            },
                            errorMessage = beneficiaryBankEM,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        InformationLine(
                            label = "Beneficiary account/card",
                            placeholder = "Account/card number",
                            value = beneficiaryAccount,
                            onValueChange = { beneficiaryAccount = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            errorMessage = beneficiaryAccountEM,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text(
                            "Transaction Information",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                    item {
                        InformationLine(
                            label = "Amount",
                            placeholder = "Enter amount",
                            value = "${formatCurrencyVN(amount)}đ",
                            onValueChange = {
                                val raw = it.replace(".", "").replace("đ", "").trim()
                                amount = if (raw.isEmpty()) {
                                    BigDecimal.ZERO
                                } else {
                                    raw.toBigDecimal()
                                }
                            },
                            suffix = {
                                VerticalDivider(
                                    modifier = Modifier
                                        .fillMaxHeight(0.8f),
                                    color = Color.Gray
                                )
                                Text(
                                    text = "VND",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            errorMessage = amountEM,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        InformationLine(
                            value = content,
                            onValueChange = { content = it },
                            label = "Content",
                            placeholder = "Enter transaction content",
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        InformationSelect(
                            label = "Transaction category",
                            placeholder = "Select by purpose",
                            options = categories,
                            onOptionSelected = { category = it },
                            suffix = {
                                VerticalDivider(
                                    modifier = Modifier
                                        .fillMaxHeight(0.8f),
                                    color = Color.Gray
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Transaction Category"
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                    item {
                        val coroutineScope = rememberCoroutineScope()

                        GreenGradientButton(
                            onButtonClick = {
                                coroutineScope.launch {
                                    val existCard =
                                        checkCardNumberExistsInCollections(beneficiaryAccount)
                                    if (beneficiaryBank == "") {
                                        beneficiaryBankEM = "Please select beneficiary bank"
                                    } else if (beneficiaryAccount == customerUiState.checkingCardNumber) {
                                        beneficiaryAccountEM =
                                            "Cannot transfer money in the same card"
                                    } else if (beneficiaryAccount == "") {
                                        beneficiaryAccountEM =
                                            "Please enter beneficiary card number"
                                    } else if (!existCard) {
                                        beneficiaryAccountEM = "Card does not exist"
                                    } else if (amount <= BigDecimal.valueOf(5000.0)) {
                                        amountEM = "Transaction amount must higher than 5000đ"
                                    } else if (amount >= customerUiState.checkingBalance) {
                                        amountEM = "Insufficient checking balance"
                                    } else {
                                        beneficiaryBankEM = ""
                                        beneficiaryAccountEM = ""
                                        amountEM = ""

                                        customerViewModel.onContinueTransactionClick(
                                            bank = beneficiaryBank,
                                            sourceCard = customerUiState.checkingCardNumber,
                                            amount = amount,
                                            content = content,
                                            category = category,
                                            destinationCard = beneficiaryAccount,
                                            navController = navController
                                        )
                                    }
                                }
                            },
                            buttonCustom = {
                                Text(
                                    text = "Continue",
                                    fontSize = 20.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
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
fun TransferScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    TransferScreen(fakeViewModel, fakeNavController)
}