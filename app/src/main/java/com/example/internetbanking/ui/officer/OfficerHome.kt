package com.example.internetbanking.ui.officer

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Percent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.internetbanking.AppScreen
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.AppAlertDialog
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.shared.toReadableDateTime
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_light_green2
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.LoginViewModel
import com.example.internetbanking.viewmodels.OfficerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficerHome(
    officerViewModel: OfficerViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController,
) {
    val context: Context = LocalContext.current

    val officerUiState by officerViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    var newProfitableRatesValue by remember { mutableStateOf("") }
    var isShowAlertDialog by remember { mutableStateOf(false) }
    var customerCardNumber by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        officerViewModel.loadLatestRates()
    }

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
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Green Vault Digibank",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = custom_mint_green
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                // TODO: LOGOUT EVENT
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
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
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Image(
                        painter = painterResource(R.drawable.app_background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Officer:",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = loginUiState.currentUser.fullName,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(240.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "ID:",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = loginUiState.currentUser.accountId,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(240.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Function:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = custom_dark_green
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White
                        )
                ) {
                    Box (
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(0.5f)
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                brush = GradientColors.Green_VerticalLightToDark
                            )
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Profitable Rates:",
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = officerUiState.profitableRates.toString(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 50.sp,
                                    modifier = Modifier.padding(end = 5.dp)
                                )
                                Text(
                                    text = "%",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                    Box (
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()

                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp)
                                .padding(top = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .height(40.dp)
                                        .fillMaxWidth(0.8f)
                                        .border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 5.dp)
                                ) {
                                    BasicTextField(
                                        value = newProfitableRatesValue,
                                        onValueChange = { newProfitableRatesValue = it },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Filled.Percent,
                                    contentDescription = "Profitable Rates",
                                    tint = custom_dark_green
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
                                    containerColor = custom_light_green2
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
                        }
                    }
                    AppAlertDialog(
                        isShow = isShowAlertDialog,
                        title = "Confirm Profitable Rates Change",
                        content = "Are you sure you want to change profitable rates to ${newProfitableRatesValue}%",
                        onConfirm = {
                            officerViewModel.onChangeRatesConfirm(newProfitableRatesValue)
                            newProfitableRatesValue = ""
                            isShowAlertDialog = false
                        },
                        onDismiss = {
                            newProfitableRatesValue = ""
                            officerViewModel.clearErrorMessage()
                            isShowAlertDialog = false
                        }
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Updated: ${officerUiState.ratesChangeTimestamp.toReadableDateTime()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            brush = GradientColors.Green_DarkToLight
                        )
                        .clickable {
                            navController.navigate(AppScreen.CreateCustomer.name)
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Create customer",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Create customer",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.create_customer),
                            contentDescription = "Create Customer"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            brush = GradientColors.Green_DarkToLight
                        )
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                    ) {
                        Text(
                            text = "Edit Customer's Information",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        InformationLine(
                            label = "Customer's card number",
                            placeholder = "Enter card number",
                            value = customerCardNumber,
                            onValueChange = { customerCardNumber = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
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

@Preview(
    showBackground = true
)
@Composable
fun OfficerHomePreview(){
    val fakeOfficerViewModel: OfficerViewModel = viewModel()
    val fakeLoginViewModel: LoginViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    OfficerHome(fakeOfficerViewModel, fakeLoginViewModel, fakeNavController)
}