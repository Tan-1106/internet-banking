package com.example.internetbanking.ui.customer

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.BillType
import com.example.internetbanking.R
import com.example.internetbanking.Service
import com.example.internetbanking.ui.shared.BalanceInformation
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayBillsScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val customerUiState by customerViewModel.uiState.collectAsState()
    var billType by remember { mutableStateOf("") }
    var customerCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
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
                            text = "Pay Bills",
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
            }, bottomBar = {
                Column {
                    if (!errorMessage.isEmpty()) {
                        Text(
                            errorMessage,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.White)
                    ) {
                        val coroutineScope = rememberCoroutineScope()

                        ElevatedButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (customerViewModel.findCheckingCardByCustomerCode(
                                            customerCode = customerCode,
                                            billType = billType
                                        )
                                    ) {
                                        val payBill = customerViewModel.uiState.value.currentPayBill

                                        customerViewModel.onContinueTransactionClick(
                                            billType = billType,
                                            customerCode = customerCode,
                                            type = Service.Paybill.name,
                                            fee = BigDecimal.ZERO,
                                            provider = payBill?.provider
                                                ?: "",
                                            amount = payBill?.amount
                                                ?: BigDecimal.ZERO,
                                            destinationCard = payBill?.cardNumber
                                                ?: "",
                                            sourceCard = customerUiState.checkingCardNumber,
                                            navController = navController
                                        )
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Customer code is not valid",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
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
                    .padding(10.dp)
            ) {
                BalanceInformation(
                    cardNumber = customerUiState.checkingCardNumber,
                    balance = formatCurrencyVN(customerUiState.checkingBalance)
                )
                Spacer(modifier = Modifier.height(10.dp))
                InformationSelect(
                    label = "Bill Type",
                    placeholder = "Select bill type",
                    options = BillType.entries.map { it.name },
                    onOptionSelected = { billType = it }
                )
                InformationLine(
                    label = "Customer code ",
                    placeholder = "Enter customer code",
                    value = customerCode,
                    onValueChange = { customerCode = it },
                    isEnable = true,
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PayBillsScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    PayBillsScreen(fakeViewModel, fakeNavController)
}