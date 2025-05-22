package com.example.internetbanking.ui.customer

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.ViewProfitRatesAndProfit
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_light_green2
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyFlightTicketsScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val uiState = customerViewModel.uiState.collectAsState()
    val flightMatching = uiState.value.flightMatching
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
                            text = "Select Airline",
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
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(10.dp)
            ) {
                if (flightMatching.isEmpty()) {
                    item {
                        Text(
                            text = "No flight found",
                            fontSize = 20.sp,
                            color = custom_light_green1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {


                    items(items = flightMatching) {
                        FlightCard(
                            image = {
                                Image(
                                    painter = painterResource(R.drawable.logo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(5.dp)
                                )
                            },
                            nameAirport = it.flightProvider,
                            departureLocation = it.from,
                            departureTime = it.startTime,
                            arrivalLocation = it.to,
                            arrivalTime = it.endTime,
                            price = "${formatCurrencyVN(it.price)}đ",
                            onClick = {
                                Log.i("Flight", "Flight ID: ${it.flightId}")
                            }

                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }

            }
        }
    }
}

@Composable
fun FlightCard(
    image: @Composable () -> Unit,
    nameAirport: String,
    departureLocation: String,
    departureTime: String,
    arrivalLocation: String,
    arrivalTime: String,
    price: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    corner = CornerSize(10.dp)
                )
            )
            .clickable(onClick = onClick)
            .border(
                1.dp,
                color = custom_light_green1,
                shape = RoundedCornerShape(corner = CornerSize(10.dp))
            ), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //logo
            image()
            Text(
                nameAirport,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.widthIn(max = 80.dp),
                textAlign = TextAlign.Center
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.widthIn(max = 60.dp)) {
                Text(departureTime, fontWeight = FontWeight.Bold, fontSize = 20.sp)
//                Text(departureLocation)
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(10.dp)
                    .width(30.dp)
            )
            Column(modifier = Modifier.widthIn(max = 60.dp)) {
                Text(arrivalTime, fontWeight = FontWeight.Bold, fontSize = 20.sp)
//                Text(arrivalLocation)
            }
        }
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(5.dp)
            )
            Column(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    end = 10.dp,
                ),
                horizontalAlignment = Alignment.End
            ) {
                Text(price, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text(
                    "Buy",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = custom_light_green2
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun BuyFlightTicketsScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()
    BuyFlightTicketsScreen(
        fakeViewModel, fakeNavController
    )
}