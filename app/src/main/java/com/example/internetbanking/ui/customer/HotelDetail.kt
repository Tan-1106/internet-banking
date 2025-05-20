package com.example.internetbanking.ui.customer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.ui.theme.GradientColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailsScreen(
    navController: NavHostController,
    hotelName: String
) {
    val hotel = hotels.find { it.name == hotelName } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chi tiết khách sạn",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
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
            // Banner khách sạn
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = hotel.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = hotel.address,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${hotel.rating}/5 (500 đánh giá)",
                            fontSize = 14.sp,
                            color = Color(0xFFF8BBD0)
                        )
                    }
                }
            }

            // Thông tin phòng
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tổng quan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val today = Calendar.getInstance()
                    val checkIn = dateFormat.format(today.time)
                    today.add(Calendar.DAY_OF_YEAR, 1) // Add 1 day
                    val checkOut = dateFormat.format(today.time)
                    Text(
                        text = "$checkIn - $checkOut | 1 phòng, 2 khách",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    hotel.rooms.forEach { room ->
                        RoomDetailItem(room = room)
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun RoomDetailItem(
    room: RoomType
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = room.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "28m² | 2 Người lớn | 1 Giường cỡ king", // Giả định thông tin cố định, có thể thay bằng dữ liệu động
                fontSize = 14.sp
            )
            Text(
                text = "Khẳng hoan tiện",
                fontSize = 12.sp
            )
            Text(
                text = room.amenities.joinToString(" • "),
                fontSize = 12.sp,
                color = Color.Gray
            )
            val originalPrice = room.price * 1.4 // Giả định giảm 40%
            Text(
                text = "Từ ${String.format("%,.0f", originalPrice)}đ -40%",
                fontSize = 14.sp,
                color = Color(0xFFD81B60)
            )
            Text(
                text = "${String.format("%,.0f", originalPrice)}đ ${
                    String.format(
                        "%,.0f",
                        room.price
                    )
                }đ",
                fontSize = 14.sp
            )
            Text(
                text = "Tổng giá 1 phòng x 1 đêm, đã bao gồm thuế phí",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Button(
                onClick = { /* Xử lý đặt phòng */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD81B60))
            ) {
                Text(
                    text = "Đặt phòng",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HotelDetailsScreenPreview() {
    val fakeNavController: NavHostController = rememberNavController()
    HotelDetailsScreen(fakeNavController, "Grand Ocean Hotel")
}