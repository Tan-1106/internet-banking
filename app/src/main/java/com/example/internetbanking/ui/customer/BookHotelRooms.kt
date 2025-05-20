package com.example.internetbanking.ui.customer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.viewmodels.CustomerViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Dữ liệu mẫu cho khách sạn
data class Hotel(
    val name: String,
    val address: String,
    val rating: Double,
    val imageUrl: String = "",
    val rooms: List<RoomType>
)

data class RoomType(
    val name: String,
    val price: Double,
    val description: String,
    val amenities: List<String>,
    val imageUrl: String = ""
)

val hotels = listOf(
    Hotel(
        name = "Grand Ocean Hotel",
        address = "123 Đường Biển, TP.HCM",
        rating = 4.5,
        rooms = listOf(
            RoomType(
                name = "Superior Basement Room",
                price = 1895.529,
                description = "Cozy room with one king bed",
                amenities = listOf("Wi-Fi", "TV", "Air Conditioning")
            ),
            RoomType(
                name = "Deluxe Room",
                price = 2500.0,
                description = "Spacious room with city view",
                amenities = listOf("Wi-Fi", "TV", "Mini Bar")
            )
        )
    ),
    Hotel(
        name = "City View Resort",
        address = "456 Đường Núi, Đà Lạt",
        rating = 4.2,
        rooms = listOf(
            RoomType(
                name = "Standard Room",
                price = 1200.0,
                description = "Comfortable room with one double bed",
                amenities = listOf("Wi-Fi", "TV")
            )
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookHotelRoomsScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    // Trạng thái UI
    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }
    var numberOfGuests by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Danh sách khách sạn",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.background(brush = GradientColors.Green_DarkToLight)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Thanh tìm kiếm và chọn ngày
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Tìm kiếm khách sạn...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD81B60),
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DatePickerField(
                            label = "Ngày nhận phòng",
                            selectedDate = checkInDate,
                            onDateSelected = { checkInDate = it },
                            modifier = Modifier.weight(1f)
                        )
                        DatePickerField(
                            label = "Ngày trả phòng",
                            selectedDate = checkOutDate,
                            onDateSelected = { checkOutDate = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    GuestCounter(
                        numberOfGuests = numberOfGuests,
                        onGuestCountChanged = { numberOfGuests = it }
                    )

                    Button(
                        onClick = { /* Tìm kiếm khách sạn */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD81B60))
                    ) {
                        Text(
                            text = "Tìm khách sạn",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Danh sách khách sạn
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "Danh sách khách sạn",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            items(hotels) { hotel ->
                HotelItem(
                    hotel = hotel,
                    onClick = { navController.navigate("hotelDetails/${hotel.name}") }
                )
            }
        }
    }
}

@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(label) },
        modifier = modifier
            .clickable {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val today = Calendar.getInstance()
                onDateSelected(dateFormat.format(today.time))
            },
        readOnly = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFD81B60),
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Composable
fun GuestCounter(
    numberOfGuests: Int,
    onGuestCountChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Số lượng khách", fontSize = 16.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (numberOfGuests > 1) onGuestCountChanged(numberOfGuests - 1) },
                    enabled = numberOfGuests > 1
                ) {
                    Text("-", fontSize = 20.sp, color = Color(0xFFD81B60))
                }
                Text(numberOfGuests.toString(), fontSize = 16.sp)
                IconButton(
                    onClick = { onGuestCountChanged(numberOfGuests + 1) }
                ) {
                    Text("+", fontSize = 20.sp, color = Color(0xFFD81B60))
                }
            }
        }
    }
}

@Composable
fun HotelItem(
    hotel: Hotel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = hotel.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = hotel.address,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${hotel.rating}/5 (500 đánh giá)",
                    fontSize = 12.sp,
                    color = Color(0xFFF8BBD0)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookHotelRoomsScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()
    BookHotelRoomsScreen(fakeViewModel, fakeNavController)
}