package com.example.internetbanking.ui.customer

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.Service
import com.example.internetbanking.ui.shared.BalanceInformation
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_light_green2
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositPhoneMoneyScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current
    val customerUiState by customerViewModel.uiState.collectAsState()

    var phoneNumber by remember { mutableStateOf("") }
    var phoneMoney by remember { mutableStateOf("") }
    var selectedAmount by remember { mutableStateOf<String?>(null) }
    val amounts = listOf(
        "20.000đ",
        "50.000đ",
        "100.000đ",
        "200.000đ",
        "500.000đ",
        "1.000.000đ",
    )
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
                            text = "Deposit phone",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = custom_mint_green
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = custom_mint_green
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .background(
                            brush = GradientColors.Green_DarkToLight
                        ),
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White)
                ) {
                    ElevatedButton(
                        onClick = {
                            if (phoneNumber.length < 10 || phoneNumber.length > 11) {
                                Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
                            } else if (phoneMoney.isEmpty()) {
                                Toast.makeText(context, "Please enter top up value", Toast.LENGTH_SHORT).show()
                            } else {
                                customerViewModel.onContinueTransactionClick(
                                    type = Service.DepositPhoneMoney.name,
                                    amount = phoneMoney.toBigDecimal(),
                                    sourceCard = customerUiState.checkingCardNumber,
                                    destinationCard = "9659054023",
                                    network = "Vinaphone",
                                    destinationPhoneNumber = phoneNumber,
                                    navController = navController
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
                            text = "Continue",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            },
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(10.dp)
            ) {
                BalanceInformation(
                    cardNumber = customerUiState.checkingCardNumber,
                    balance = formatCurrencyVN(customerUiState.checkingBalance)
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Transaction Information",
                    color = custom_light_green2,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Spacer(Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(
                                corner = CornerSize(20)
                            )
                        )
                ) {
                    Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                        TextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Contacts,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(
                                corner = CornerSize(20.dp)
                            )
                        )
                        .padding(10.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        items(items = amounts) { amount ->
                            AmountOption(
                                amount = amount,
                                isSelected = selectedAmount == amount,
                                onClick = {
                                    selectedAmount = amount
                                    val raw = amount.replace(".", "").replace("đ", "").trim()
                                    phoneMoney = raw
                                },
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmountOption(
    amount: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (isSelected) custom_dark_green else Color.Gray,
                shape = RoundedCornerShape(corner = CornerSize(10.dp))
            )
            .clickable { onClick() }
            .background(
                color = if (isSelected) custom_dark_green.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(corner = CornerSize(10.dp))
            )
            .padding(10.dp)
    ) {
        Text(
            text = amount,
            fontSize = 12.sp,
            color = if (isSelected) custom_dark_green else Color.Black,
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Transparent)
        )
    }
}

@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun DepositPhoneMoneyScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    DepositPhoneMoneyScreen(fakeViewModel, fakeNavController)
}