package com.example.internetbanking.ui.customer


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.AppScreen
import com.example.internetbanking.Service
import com.example.internetbanking.ui.shared.DatePicker
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

data class Flight(
    val airline: String,
    val departureTime: String,
    val arrivalTime: String,
    val price: Double,
    val duration: String
)

data class Airport(
    val code: String,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindFlightScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current
    val customerUiState by customerViewModel.uiState.collectAsState()


    val airports = listOf(
        Airport("SGN", "Tan Son Nhat, Ho Chi Minh City"),
        Airport("HAN", "Noi Bai, Hanoi"),
        Airport("DAD", "Da Nang International, Da Nang")
    )

    val flights = listOf(
        Flight("Vietnam Airlines", "08:00", "10:00", 120.0, "2h"),
        Flight("VietJet Air", "09:30", "11:30", 90.0, "2h"),
        Flight("Bamboo Airways", "12:00", "14:00", 110.0, "2h")
    )

    var departureAirport by remember { mutableStateOf<Airport?>(null) }
    var arrivalAirport by remember { mutableStateOf<Airport?>(null) }
    var departureDate by remember { mutableStateOf("") }
    var numberOfPassengers by remember { mutableStateOf("") }
    var ticketClass by remember { mutableStateOf("Economy") }
    var selectedFlight by remember { mutableStateOf<Flight?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Buy Flight Tickers",
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
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                InformationSelect(
                    label = "Departure Airport",
                    placeholder = "Select departure airport",
                    options = airports.map { it.name },
                    onOptionSelected = { selectedAirport ->
                        departureAirport = airports.find { it.name == selectedAirport }
                    },
                    suffix = {
                        VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "")
                    }
                )
            }
            item {
                InformationSelect(
                    label = "Arrival Airport",
                    placeholder = "Select arrival airport",
                    options = airports.map { it.name },
                    onOptionSelected = { selectedAirport ->
                        arrivalAirport = airports.find { it.name == selectedAirport }
                    },
                    suffix = {
                        VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "")
                    }
                )
            }
            item {
                DatePicker(
                    label = "Departure Date",
                    placeholder = "Select departure date",
                    onDatePick = { departureDate = it },
                    canSelectFuture = true,
                    suffix = {
                        VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                        Icon(Icons.Filled.DateRange, contentDescription = "Select birthday")
                    }
                )
            }
            item {
                InformationLine(
                    label = "Number passenger",
                    placeholder = "Enter number passenger",
                    value = numberOfPassengers,
                    onValueChange = { numberOfPassengers = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            item {
                InformationSelect(
                    label = "Ticket Class",
                    placeholder = ticketClass,
                    options = listOf("Economy", "Business", "First Class"),
                    onOptionSelected = { ticketClass = it },
                    suffix = {
                        VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "")
                    }
                )
            }
            item {
                Text(
                    text = "Available Flights",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                FlightList(
                    flights = flights,
                    selectedFlight = selectedFlight,
                    onFlightSelected = { flight -> selectedFlight = flight }
                )
            }
            item {
                Button(
                    onClick = {
                        if (departureAirport != null && arrivalAirport != null &&
                            departureDate.isNotEmpty() && selectedFlight != null
                        ) {
                            if (departureAirport == arrivalAirport) {
                                Toast.makeText(context, "Invalid airport selected", Toast.LENGTH_SHORT).show()
                            } else {
//
                                navController.navigate(AppScreen.Flight.name)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = departureAirport != null && arrivalAirport != null &&
                            departureDate.isNotEmpty() && selectedFlight != null
                ) {
                    Text("Book Now", fontSize = 16.sp)
                }
            }
        }
    }
}


@Composable
fun FlightList(
    flights: List<Flight>,
    selectedFlight: Flight?,
    onFlightSelected: (Flight) -> Unit
) {
    LazyColumn(
        modifier = Modifier.height(200.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(flights) { flight ->
            FlightItem(
                flight = flight,
                isSelected = flight == selectedFlight,
                onClick = { onFlightSelected(flight) }
            )
        }
    }
}

@Composable
fun FlightItem(
    flight: Flight,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = flight.airline,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${flight.departureTime} - ${flight.arrivalTime}",
                    fontSize = 14.sp
                )
                Text(
                    text = flight.duration,
                    fontSize = 14.sp
                )
            }
            Text(
                text = "$${flight.price}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FindFlightScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    FindFlightScreen(fakeViewModel, fakeNavController)
}