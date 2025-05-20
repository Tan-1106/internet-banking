package com.example.internetbanking.ui.customer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel
import java.math.BigDecimal

val DarkMintGreen = Color(0xFF2E7D32)
val LightDarkMintGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    navController: NavHostController,
    customerViewModel: CustomerViewModel
) {
    var amount by remember { mutableStateOf(BigDecimal.valueOf(50000)) }
    var cardNumber by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }

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

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Card number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = DarkMintGreen,
                    unfocusedLabelColor = Color.Black,
                    focusedIndicatorColor = DarkMintGreen,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it },
                label = { Text("Bank") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = DarkMintGreen,
                    unfocusedLabelColor = Color.Black,
                    focusedIndicatorColor = DarkMintGreen,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            OutlinedTextField(
                value = cardHolderName,
                onValueChange = { cardHolderName = it },
                label = { Text("Owner's name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = DarkMintGreen,
                    unfocusedLabelColor = Color.Black,
                    focusedIndicatorColor = DarkMintGreen,
                    unfocusedIndicatorColor = Color.LightGray
                )
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
                            text = "${formatCurrencyVN(amount)} đ",
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
                    text = "${formatCurrencyVN(amount)} đ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkMintGreen
                )
            }

            Button(
                onClick = {
                    //TODO: CONFIRM

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkMintGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirm",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    val fakeNavController: NavHostController = rememberNavController()
    val fakeViewModel: CustomerViewModel = viewModel()
    TransactionScreen(fakeNavController, fakeViewModel)
}