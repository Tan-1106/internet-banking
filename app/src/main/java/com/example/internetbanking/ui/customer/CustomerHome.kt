package com.example.internetbanking.ui.customer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.AppScreen
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.LogoutDialog
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel
import com.example.internetbanking.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHome(
    customerViewModel: CustomerViewModel,
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    // UiStates
    val customerUiState by customerViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsState()

    // Logout Dialog
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Load Data
    LaunchedEffect(Unit) {
        customerViewModel.loadCustomerInformation(loginUiState.currentUser)
    }
    val currentViewType = customerUiState.currentViewType
    val cardNumber = if (currentViewType == "Checking") {
        customerUiState.checkingCardNumber
    } else if (currentViewType == "Saving") {
        customerUiState.savingCardNumber
    } else {
        customerUiState.mortgageCardNumber
    }

    // Function's Variables
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    @Suppress("DEPRECATION") val clipboardManager = LocalClipboardManager.current
    var isHiddenBalance by remember { mutableStateOf(true) }

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
        LogoutDialog(
            showDialog = showLogoutDialog,
            onDismiss = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false
                loginViewModel.logout(navController)
            }
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
                                showLogoutDialog = true
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
            snackbarHost = { SnackbarHost(snackbarHostState) },
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
                // VIEW BALANCE FOR CHECKING & SAVING
                val boxHeight = if (currentViewType == "Checking" || currentViewType == "Saving") {
                    Modifier.fillMaxHeight(0.2f)
                } else {
                    Modifier.fillMaxHeight(0.15f)
                }
                Box(
                    modifier = Modifier
                        .then(boxHeight)
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp
                            )
                        )
                        .border(
                            color = custom_dark_green,
                            width = 1.dp,
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(R.drawable.app_background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        contentAlignment = Alignment.TopEnd,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "View Profile",
                            tint = Color.White,
                            modifier = Modifier.clickable {
                                // View Customer Profile
                                navController.navigate(AppScreen.Profile.name)
                            }
                        )
                    }
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
                                text = "Hello, ",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                            Text(
                                text = customerUiState.account.fullName,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        // ACCOUNT NUMBER:
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Account:",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.width(240.dp)
                            ) {
                                Text(
                                    text = cardNumber,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = "Copy Account Number",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            val accountNumber = cardNumber
                                            clipboardManager.setText(AnnotatedString(accountNumber))
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Copied: $accountNumber")
                                            }
                                        }
                                )
                            }
                        }
                        // VIEW BALANCE FOR CHECKING & SAVING
                        if (currentViewType == "Checking" || currentViewType == "Saving") {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Balance:",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.width(240.dp)
                                ) {
                                    Text(
                                        text = if (isHiddenBalance) "**********" else if (currentViewType == "Checking") {
                                            "${customerUiState.checkingBalance} VND"
                                        } else {
                                            "${customerUiState.savingBalance} VND"
                                        },
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                        imageVector = if (isHiddenBalance) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = "Balance Showing",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
                                                isHiddenBalance = !isHiddenBalance
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            )
                        )
                        .background(
                            brush = GradientColors.Green_LightToDark
                        )
                        .border(
                            color = custom_dark_green,
                            width = 1.dp,
                            shape = RoundedCornerShape(
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            )
                        )
                        .clickable {
                            // Transaction History
                            navController.navigate(AppScreen.TransactionHistory.name)
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = "Transaction History",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Transaction history",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Functions:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = custom_dark_green
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.deposit,
                            functionName = "Deposit",
                            onFunctionClick = {
                                // Deposit Money
                                navController.navigate(AppScreen.Deposit.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.withdraw,
                            functionName = "Withdraw",
                            onFunctionClick = {
                                // Withdraw Money
                                navController.navigate(AppScreen.Withdraw.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.domestic_transfer,
                            functionName = "Transfer",
                            onFunctionClick = {
                                // Transfer Money
                                navController.navigate(AppScreen.Transfer.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.pay_bill,
                            functionName = "Pay bills",
                            onFunctionClick = {
                                // Pay Bill
                                navController.navigate(AppScreen.PayBills.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.deposit_phone,
                            functionName = "Mobile topup",
                            onFunctionClick = {
                                // Mobile TopUp
                                navController.navigate(AppScreen.DepositPhoneMoney.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.plane_ticket,
                            functionName = "Book flight",
                            onFunctionClick = {
                                // Book Flight
                                navController.navigate(AppScreen.BuyFlightTickets.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.movie_ticket,
                            functionName = "Movie ticket",
                            onFunctionClick = {
                                // Movie Ticket
                                navController.navigate(AppScreen.BuyMovieTickets.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.hotel_room,
                            functionName = "Book hotel",
                            onFunctionClick = {
                                // Hotel Room
                                navController.navigate(AppScreen.BookHotelRooms.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.ecommerce,
                            functionName = "Ecommerce",
                            onFunctionClick = {
                                // Ecommerce
                                navController.navigate(AppScreen.Ecommerce.name)
                            }
                        )
                    }
                    item {
                        FunctionComponent(
                            functionIcon = R.drawable.bank_location,
                            functionName = "Bank offices",
                            onFunctionClick = {
                                // Ecommerce
                                navController.navigate(AppScreen.LocateUserAndBank.name)
                            }
                        )
                    }
                    if (customerViewModel.hasMortgage) {
                        item {
                            FunctionComponent(
                                functionIcon = R.drawable.mortgage_money,
                                functionName = "Installment",
                                onFunctionClick = {
                                    // Ecommerce
                                    navController.navigate(AppScreen.ViewMortgageMoney.name)
                                }
                            )
                        }
                    }
                    if (customerViewModel.hasSaving ) {
                        item {
                            FunctionComponent(
                                functionIcon = R.drawable.profits,
                                functionName = "Rates & profits",
                                onFunctionClick = {
                                    // Ecommerce
                                    navController.navigate(AppScreen.ViewProfitsAndRates.name)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FunctionComponent(
    @DrawableRes functionIcon: Int,
    functionName: String,
    onFunctionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(functionIcon),
            contentDescription = functionName,
            modifier = Modifier
                .size(50.dp)
                .clip(
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable (
                    onClick = onFunctionClick
                )
        )
        Text(
            text = functionName,
            fontSize = 12.sp
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CustomerHomePreview(){
    val fakeCustomerViewModel: CustomerViewModel = viewModel()
    val fakeLoginViewModel: LoginViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    CustomerHome(fakeCustomerViewModel, fakeLoginViewModel, fakeNavController)
}