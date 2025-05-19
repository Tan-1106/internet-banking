package com.example.internetbanking.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
fun BuyFlightTicketsScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    // Danh sách sân bay mẫu
    val airports = listOf(
        Airport("SGN", "Tan Son Nhat, Ho Chi Minh City"),
        Airport("HAN", "Noi Bai, Hanoi"),
        Airport("DAD", "Da Nang International, Da Nang")
    )

    // Danh sách chuyến bay mẫu
    val flights = listOf(
        Flight("Vietnam Airlines", "08:00", "10:00", 120.0, "2h"),
        Flight("VietJet Air", "09:30", "11:30", 90.0, "2h"),
        Flight("Bamboo Airways", "12:00", "14:00", 110.0, "2h")
    )

    // Trạng thái UI
    var departureAirport by remember { mutableStateOf<Airport?>(null) }
    var arrivalAirport by remember { mutableStateOf<Airport?>(null) }
    var departureDate by remember { mutableStateOf("") }
    var numberOfPassengers by remember { mutableStateOf(1) }
    var ticketClass by remember { mutableStateOf("Economy") }
    var selectedFlight by remember { mutableStateOf<Flight?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buy Flight Tickets") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Chọn sân bay đi
            AirportPicker(
                label = "Departure Airport",
                selectedAirport = departureAirport,
                airports = airports,
                onAirportSelected = { airport -> departureAirport = airport }
            )

            // Chọn sân bay đến
            AirportPicker(
                label = "Arrival Airport",
                selectedAirport = arrivalAirport,
                airports = airports,
                onAirportSelected = { airport -> arrivalAirport = airport }
            )

            // Chọn ngày bay
            DatePickerField(
                label = "Departure Date",
                selectedDate = departureDate,
                onDateSelected = { date -> departureDate = date }
            )

            // Số lượng hành khách
            PassengerCounter(
                numberOfPassengers = numberOfPassengers,
                onPassengerCountChanged = { count -> numberOfPassengers = count }
            )

            // Chọn loại vé
            TicketClassSelector(
                selectedClass = ticketClass,
                onClassSelected = { ticket -> ticketClass = ticket }
            )

            // Danh sách chuyến bay
            Text(
                text = "Available Flights",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            FlightList(
                flights = flights,
                selectedFlight = selectedFlight,
                onFlightSelected = { flight -> selectedFlight = flight }
            )

            // Nút xác nhận
            Button(
                onClick = {
                    if (departureAirport != null && arrivalAirport != null &&
                        departureDate.isNotEmpty() && selectedFlight != null
                    ) {
                        // Xử lý đặt vé (gửi dữ liệu đến ViewModel hoặc navigation)
                        navController.navigate("confirmation")
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

@Composable
fun AirportPicker(
    label: String,
    selectedAirport: Airport?,
    airports: List<Airport>,
    onAirportSelected: (Airport) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedAirport?.name ?: "",
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            readOnly = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            airports.forEach { airport ->
                DropdownMenuItem(
                    text = { Text("${airport.code} - ${airport.name}") },
                    onClick = {
                        onAirportSelected(airport)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PassengerCounter(
    numberOfPassengers: Int,
    onPassengerCountChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Number of Passengers", fontSize = 16.sp)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (numberOfPassengers > 1) onPassengerCountChanged(numberOfPassengers - 1) },
                enabled = numberOfPassengers > 1
            ) {
                Text("-", fontSize = 20.sp)
            }
            Text(numberOfPassengers.toString(), fontSize = 16.sp)
            IconButton(
                onClick = { onPassengerCountChanged(numberOfPassengers + 1) }
            ) {
                Text("+", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun TicketClassSelector(
    selectedClass: String,
    onClassSelected: (String) -> Unit
) {
    val ticketClasses = listOf("Economy", "Business", "First Class")
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedClass,
            onValueChange = {},
            label = { Text("Ticket Class") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            readOnly = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            ticketClasses.forEach { ticketClass ->
                DropdownMenuItem(
                    text = { Text(ticketClass) },
                    onClick = {
                        onClassSelected(ticketClass)
                        expanded = false
                    }
                )
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
fun BuyFlightTicketsScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    BuyFlightTicketsScreen(fakeViewModel, fakeNavController)
}