package com.example.internetbanking.ui.customer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel
import java.math.BigDecimal

val DarkMintGreen = Color(0xFF2E7D32)
val LightDarkMintGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    navController: NavHostController,
    customerViewModel: CustomerViewModel,
) {
    val banks = listOf<String>("Vietcombank", "Sacombank", "Techcombank", "Agribank", "VietinBank")
    val context = LocalContext.current
    var cardNumber by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    val customerUiState by customerViewModel.uiState.collectAsState()
    var errorMessage by remember { mutableStateOf("") }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Secure payment",
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
        bottomBar = {
            Column(horizontalAlignment = Alignment.Start) {
                if (!errorMessage.isEmpty()) {
                    Text(errorMessage, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 10.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White)
                ) {

                    ElevatedButton(
                        onClick = {
                            errorMessage = validateCard(
                                cardNumber = cardNumber,
                                bankName = bankName,
                                ownerName = ownerName
                            )
                            if (errorMessage.isEmpty()) {
                                customerViewModel.onConfirmDeposit(
                                    cardNumber = cardNumber,
                                    bank = bankName,
                                    ownerName = ownerName,
                                    context = context,
                                    navController = navController,
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .fillMaxSize(),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = custom_light_green1
                        )
                    ) {
                        Text(
                            text = "Confirm",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Account/Card",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            InformationLine(
                label = "Card Number",
                placeholder = "Enter card number",
                value = cardNumber,
                onValueChange = {
                    cardNumber = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )
            Spacer(Modifier.height(10.dp))
            InformationSelect(
                label = "Bank",
                placeholder = "Select bank",
                options = banks,
                onOptionSelected = {
                    bankName = it
                },
                suffix = {
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(0.8f),
                        color = Color.Gray
                    )
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                },
            )
            Spacer(Modifier.height(10.dp))
            InformationLine(
                label = "Owner's name",
                placeholder = "Enter owner's name",
                value = ownerName,
                onValueChange = {
                    ownerName = it
                },

                )

            Text(
                text = "Transaction details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Service", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = "Nạp tiền vào Ví MoMo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Bank", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = if (bankName.isNotEmpty()) bankName else "Techcombank",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Amount", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = "${formatCurrencyVN(customerUiState.currentTransaction!!.amount)} đ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Transaction Fee", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = "Free",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                }
            }

            // Tổng tiền
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total amount", fontSize = 16.sp, color = Color.Black)
                Text(
                    text = "${formatCurrencyVN(customerUiState.currentTransaction!!.amount + customerUiState.currentTransaction!!.fee)}đ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkMintGreen
                )
            }

        }
    }
}

fun validateCard(
    cardNumber: String,
    bankName: String,
    ownerName: String
): String {
    val minCardNumberLenght = 10;
    if (cardNumber.isEmpty() || cardNumber.length < minCardNumberLenght) {
        return "Card number must be at least $minCardNumberLenght characters"
    }
    if (bankName.isEmpty()) {
        return "Bank name must be selected"
    }
    if (ownerName.isEmpty()) {
        return "Owner name must be entered"
    }
    return ""
}

//@Preview(showBackground = true)
//@Composable
//fun TransactionScreenPreview() {
//    val fakeNavController: NavHostController = rememberNavController()
//    val fakeViewModel: CustomerViewModel = viewModel()
//    TransactionScreen(fakeNavController, fakeViewModel)
//}