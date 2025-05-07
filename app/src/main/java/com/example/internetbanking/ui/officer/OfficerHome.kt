package com.example.internetbanking.ui.officer

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.internetbanking.ui.shared.AppAlertDialog
import com.example.internetbanking.ui.shared.GradientBackground
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_light_green2
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.OfficerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficerHome(
    officerViewModel: OfficerViewModel,
    navController: NavHostController,
) {
    val context: Context = LocalContext.current
    val officerUiState by officerViewModel.uiState.collectAsState()
    var newProfitableRatesValue by remember { mutableStateOf("") }
    var isShowAlertDialog by remember { mutableStateOf(false) }
    var customerCardNumber by remember { mutableStateOf("") }

    Scaffold(
        containerColor = custom_mint_green,
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_2_mint_green),
                        contentDescription = "LB Digital",
                        modifier = Modifier.width(100.dp)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { //TODO: LogOut

                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = custom_mint_green
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .background(
                        brush = GradientColors.GreenRipple
                    )
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            GradientBackground(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 20.dp)
                    .fillMaxHeight(0.125f)
                    .clip(
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Officer:",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.width(100.dp)
                        )
                        Text(
                            text = officerUiState.officer.fullName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Account ID:",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.width(100.dp)
                        )
                        Text(
                            text = officerUiState.officer.userId,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = GradientColors.GreenDarkToLight
                    )
            ) {
                Text(
                    text = "FEATURES",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                        .background(
                            brush = GradientColors.VerticalGreenDarkToLight
                        )
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Current profitable rates",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${officerUiState.profitableRates}%",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Box(
                            modifier = Modifier.height(120.dp)
                        ) {
                            InformationLine(
                                label = "Change profitable rates",
                                value = newProfitableRatesValue,
                                placeholder = "Enter new profitable rates",
                                onValueChange = { newProfitableRatesValue = it },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                )
                            )
                        }
                        Button(
                            onClick = {
                                if(officerViewModel.onValidateNewRateInput(newProfitableRatesValue, context)) {
                                    isShowAlertDialog = true
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = custom_dark_green
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Change",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        AppAlertDialog(
                            isShow = isShowAlertDialog,
                            title = "Confirm Profitable Rates Change",
                            content = "Are you sure you want to change profitable rates to ${newProfitableRatesValue}%",
                            onConfirm = {
                                officerViewModel.onChangeRatesConfirm(newProfitableRatesValue)
                            },
                            onDismiss = {
                                newProfitableRatesValue = ""
                                officerViewModel.clearErrorMessage()
                                isShowAlertDialog = false
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = custom_light_green2
                        ),
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = custom_dark_green,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .fillMaxWidth()
                            .fillMaxHeight(0.2f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PersonAdd,
                                contentDescription = "Create Customer",
                                tint = custom_dark_green
                            )
                            Text(
                                text = "Create new customer",
                                color = custom_dark_green,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(12.dp)
                            )
                            .fillMaxSize()
                            .background(
                                brush = GradientColors.VerticalGreenDarkToLight
                            )
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Edit customer's information",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            InformationLine(
                                label = "Customer's card number",
                                placeholder = "Enter card number",
                                value = customerCardNumber,
                                onValueChange = { customerCardNumber = it },
                                suffix = {
                                    IconButton(
                                        onClick = {
                                            officerViewModel.onSearchClick(customerCardNumber, context, navController)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "Search Customer's Information",
                                            tint = custom_dark_green
                                        )
                                    }
                                }
                            )
                        }
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
fun OfficerHomePreview(){
    val fakeViewModel: OfficerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    OfficerHome(fakeViewModel, fakeNavController)
}