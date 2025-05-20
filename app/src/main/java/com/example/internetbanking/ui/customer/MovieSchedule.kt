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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScheduleScreen(movieTitle: String, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(movieTitle)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Banner quảng cáo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFF8BBD0), RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Thứ 3, 3 ngày",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "65K",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD81B60)
                    )
                    Text(
                        text = "tại Lotte Cinema",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            // Lịch chiếu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("H.nay", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "C.Nhật", "Thứ 2")
                val dates = listOf("20", "21", "22", "23", "24", "25", "26")
                days.forEachIndexed { index, day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Handle date selection */ }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            color = if (day == "Thứ 6") Color(0xFFD81B60) else Color.Black
                        )
                        Text(
                            text = dates[index],
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (day == "Thứ 6") Color.White else Color.Black,
                            modifier = Modifier
                                .background(
                                    if (day == "Thứ 6") Color(0xFFD81B60) else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Suất chiếu (chọn được)
            var selectedTime by remember { mutableStateOf<String?>("Tất cả") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { selectedTime = "Tất cả" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTime == "Tất cả") Color(0xFFD81B60) else Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text(
                        "Tất cả",
                        fontSize = 14.sp,
                        color = if (selectedTime == "Tất cả") Color.White else Color.Black,
                        fontWeight = if (selectedTime == "Tất cả") FontWeight.Bold else FontWeight.Normal
                    )
                }
                Text(
                    text = "9:00 - 12:00",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTime = "9:00 - 12:00"
                        }
                        .background(
                            color = if (selectedTime == "9:00 - 12:00") Color(0xFFD81B60) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                    color = if (selectedTime == "9:00 - 12:00") Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "12:00 - 15:00",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTime = "12:00 - 15:00"
                        }
                        .background(
                            color = if (selectedTime == "12:00 - 15:00") Color(0xFFD81B60) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                    color = if (selectedTime == "12:00 - 15:00") Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            // Danh sách rạp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Rạp đề xuất (5)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TP.HCM",
                        fontSize = 14.sp,
                        color = Color(0xFFD81B60),
                        modifier = Modifier
                            .clickable { /* Handle location selection */ }
                            .padding(8.dp)
                    )
                }
                TheaterItem(
                    name = "CGV Vivo City",
                    distance = "0.6 km",
                    address = "Tầng 5 | Trung tâm thương mại SC Vivo... Tìm đường",
                    showTimes = listOf(
                        ShowTime("2D Phụ đề", listOf("17:50 - 19:56"), "Còn 163/174"),
                        ShowTime(
                            "2D Lồng tiếng",
                            listOf(
                                "10:20 - 12:26",
                                "12:30 - 14:36",
                                "14:40 - 16:46",
                                "16:50 - 18:56",
                                "19:00 - 21:06",
                                "20:00 - 22:06"
                            ),
                            "Còn 268/268"
                        )
                    )
                )
                TheaterItem(
                    name = "Lotte Nam Sài Gòn",
                    distance = "0.6 km",
                    address = "Tầng 5 | Trung tâm thương mại Lotte Nam... Tìm đường",
                    showTimes = emptyList()
                )
                TheaterItem(
                    name = "CGV Crescent Mall",
                    distance = "2.1 km",
                    address = "Tầng 5 | Trung tâm thương mại Crescent Mall... Tìm đường",
                    showTimes = emptyList()
                )
            }
        }
    }
}

data class ShowTime(val type: String, val times: List<String>, val seats: String)

@Composable
fun TheaterItem(
    name: String,
    distance: String,
    address: String,
    showTimes: List<ShowTime>
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Bán ở gần rạp nay • $distance",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = address,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle",
                    tint = Color.Black
                )
            }

            if (expanded && showTimes.isNotEmpty()) {
                showTimes.forEach { showTime ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = showTime.type,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(showTime.times) { time ->
                            Button(
                                onClick = { /* Handle time slot selection */ },
                                modifier = Modifier
                                    .height(32.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFD81B60
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = time, fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                    Text(
                        text = showTime.seats,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieScheduleScreenPreview() {
    val navController = rememberNavController()
    MovieScheduleScreen(
        movieTitle = "Doraemon Movie 44: Nobita và....",
        navController = navController
    )
}