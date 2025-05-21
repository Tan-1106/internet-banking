package com.example.internetbanking.ui.customer.saving

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.BalanceInformation
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.shared.PasswordConfirmationDialog
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSavingScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current
    val customerUiState by customerViewModel.uiState.collectAsState()

    var term by remember { mutableStateOf("Select deposit term") }
    var amount by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

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
                            text = "Create Save Account",
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White)
                ) {

                    ElevatedButton(
                        onClick = {
                            showDialog = true
                        },
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .fillMaxSize(),
                        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = custom_light_green1
                        )
                    ) {
                        Text("Continue", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                }
            },

            modifier = Modifier.systemBarsPadding()
        ) { innerPadding ->
            PasswordConfirmationDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onConfirm = { password ->
                    customerViewModel.onConfirmSavingClick(
                        term = term.toInt(),
                        amount = amount.toBigDecimal(),
                        password = password,
                        context = context,
                        navController = navController
                    )
                }
            )
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
                    label = "Deposit term",
                    placeholder = term,
                    options = listOf("1", "3", "6", "12", "24"),
                    onOptionSelected ={
                        term = it
                    },
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
                )
                InformationLine(
                    label = "Deposit Amount",
                    placeholder = "Enter deposit amount",
                    value = amount,
                    onValueChange = { amount = it },
                    isEnable = true,
                    suffix ={
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )
            }
        }
    }
}
