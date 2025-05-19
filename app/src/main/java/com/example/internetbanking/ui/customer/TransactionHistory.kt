package com.example.internetbanking.ui.customer

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green2
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    var fromDate by remember { mutableStateOf("Pick a date") }
    var toDate by remember { mutableStateOf("Pick a date") }

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

                Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                    ElevatedButton(
                        shape = RoundedCornerShape(percent = 20),
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = custom_light_green2
                        )
                    ) {
                        Text("Search")
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                    Text("Transaction History", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                ) {
                    TextButton(
                        onClick = {

                        },
                        modifier = Modifier
                            .weight(0.3f)
                            .background(
                                color = custom_light_green2,
                                shape = RoundedCornerShape(percent = 20),
                            )
                            .padding(vertical = 10.dp), contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "All",
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.width(5.dp))
                    TextButton(
                        onClick = {}, modifier = Modifier
                            .weight(0.3f)
                            .background(
                                color = custom_light_green2,
                                shape = RoundedCornerShape(percent = 20)
                            )
                            .padding(vertical = 10.dp), contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "In",
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.width(5.dp))
                    TextButton(
                        onClick = {}, modifier = Modifier
                            .weight(0.3f)
                            .background(
                                color = custom_light_green2,
                                shape = RoundedCornerShape(percent = 20)
                            )
                            .padding(vertical = 10.dp), contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Out",
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
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
                    items(count = 10) {
                        Row (modifier = Modifier.padding(5.dp)){
                            Column {
                                Text(
                                    text = "04/05/2025",
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
                                            "ABCD EFGH IJK XXXXXX",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(Modifier.width(5.dp))
                                    Column(
                                        modifier = Modifier.weight(3f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("-12.000VND")
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
