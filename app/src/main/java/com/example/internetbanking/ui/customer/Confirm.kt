package com.example.internetbanking.ui.customer

import android.content.Context
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
import androidx.compose.material3.HorizontalDivider
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
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.PasswordConfirmationDialog
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current
    val customerUiState by customerViewModel.uiState.collectAsState()
    val currentTransaction = customerUiState.checkingCurrentTransfer
    val transaction = currentTransaction.transaction

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
                            text = "Confirm",
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
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(10.dp)
            ) {
                Spacer(Modifier.height(30.dp))
                Column(
                    modifier = Modifier
                        .background(
                            color = custom_mint_green,
                            shape = RoundedCornerShape(
                                corner = CornerSize(20.dp)
                            )
                        )
                        .padding(all = 20.dp)
                ) {
                    LineConfirm(
                        label = "Service Type: ",
                        data = "Transfer"
                    )
                    Spacer(Modifier.height(20.dp))
                    LineConfirm(
                        label = "Customer Card: ",
                        data = customerUiState.checkingCardNumber
                    )
                    Spacer(Modifier.height(20.dp))
                    LineConfirm(
                        label = "Customer Name: ",
                        data = customerUiState.account.fullName
                    )
                    Spacer(Modifier.height(20.dp))
                    LineConfirm(
                        label = "Amount: ",
                        data = "${formatCurrencyVN(transaction.amount)} VND"
                    )
                    Spacer(Modifier.height(20.dp))
                    LineConfirm(
                        label = "Fee Amount: ",
                        data = "${formatCurrencyVN(transaction.fee)} VND"
                    )
                    Spacer(Modifier.height(20.dp))
                    LineConfirm(
                        label = "Content: ",
                        data = currentTransaction.content
                    )

                    PasswordConfirmationDialog(
                        showDialog = showDialog,
                        onDismiss = { showDialog = false },
                        onConfirm = { password ->
                            customerViewModel.passwordConfirm(
                                transactionDetail = currentTransaction,
                                password = password,
                                context = context,
                                navController = navController
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LineConfirm(label: String, data: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            label, fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(data, fontSize = 15.sp)
    }
    Spacer(Modifier.height(10.dp))
    HorizontalDivider(color = Color.Gray)
}

@Preview(
    showBackground = true
)
@Composable
fun ConfirmScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    ConfirmScreen(fakeViewModel, fakeNavController)
}