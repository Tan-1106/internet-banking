package com.example.internetbanking.ui.customer

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.DatePicker
import com.example.internetbanking.ui.shared.dateStringToTimestamp
import com.example.internetbanking.ui.shared.toReadableDateTime
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_light_green2
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val customerUiState by customerViewModel.uiState.collectAsState()

    var fromDate by remember { mutableStateOf("Pick a date") }
    var from = 0L
    var toDate by remember { mutableStateOf("Pick a date") }
    var to = 0L

    val transactionTypeOptions: List<String> = listOf("All", "In", "Out")
    var selectedType by remember { mutableStateOf("All") }

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
                            text = "Transaction History",
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
                            brush = GradientColors.Green_Ripple
                        )
                )
            },
            modifier = Modifier.systemBarsPadding()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement
                        .SpaceEvenly
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        DatePicker(
                            label = "From",
                            placeholder = fromDate,
                            onDatePick = {
                                fromDate = it
                                from = dateStringToTimestamp(it) ?: 0L
                            },
                            suffix = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        DatePicker(
                            label = "To",
                            placeholder = toDate,
                            onDatePick = {
                                toDate = it
                                to = dateStringToTimestamp(it) ?: 0L
                            },
                            suffix = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
                // Transaction Type
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    transactionTypeOptions.forEach { option ->
                        val isSelected = option == selectedType
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = if (isSelected) 8.dp else 2.dp,
                            shadowElevation = if (isSelected) 8.dp else 0.dp,
                            color = if (isSelected) custom_dark_green else custom_light_green2,
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .padding(horizontal = 4.dp)
                                .clickable { selectedType = option }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(0.3f)
                                    .background(
                                        color = if (isSelected) custom_dark_green else custom_light_green2,
                                        shape = RoundedCornerShape(percent = 20),
                                    )
                                    .padding(vertical = 10.dp)
                            ) {
                                Text(
                                    text = option,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                // Search
                Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                    ElevatedButton(
                        shape = RoundedCornerShape(percent = 20),
                        onClick = {
                            customerViewModel.filterTransactions(selectedType, from, to)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = custom_light_green2
                        )
                    ) {
                        Text("Search")
                    }
                }

                // List
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Transaction History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.9f)
                        .padding(5.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(4.dp)
                        )

                ) {
                    items(customerUiState.transactionHistory.size) { index ->
                        val transaction = customerUiState.transactionHistory[index]
                        Row(
                            modifier = Modifier
                                .padding(5.dp)
                                .clickable {
                                    customerViewModel.onTransactionHistoryClick(
                                        transaction,
                                        navController
                                    )
                                }
                        ) {
                            Column {
                                Text(
                                    text = transaction.timestamp.toReadableDateTime(),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(6f)
                                    ) {
                                        Text(
                                            text = transaction.content,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(Modifier.width(5.dp))
                                    Column(
                                        modifier = Modifier.weight(3f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (transaction.type == "In") {
                                                "+${transaction.amount} VND"
                                            } else {
                                                "-${transaction.amount} VND"
                                            }
                                        )
                                    }
                                    Spacer(Modifier.width(5.dp))
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowRight,
                                            contentDescription = null
                                        )
                                    }
                                }
                                Spacer(Modifier.height(5.dp))
                                HorizontalDivider()
                            }
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
fun TransactionHistoryScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    TransactionHistoryScreen(fakeViewModel, fakeNavController)
}
