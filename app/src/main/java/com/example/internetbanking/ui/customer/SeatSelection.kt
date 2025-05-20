package com.example.internetbanking.ui.customer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_light_green2
import com.example.internetbanking.ui.theme.custom_light_red
import com.example.internetbanking.ui.theme.custom_mint_green

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelectionScreen(
    theaterName: String = "",
    showTime: String = "",
    movieTitle: String = "",
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select seat",
                        fontSize = 18.sp,
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = theaterName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = showTime,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Phòng chiếu 03",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "2D Phụ đề",
                    fontSize = 14.sp,
                    color = Color(0xFFD81B60),
                    modifier = Modifier
                        .background(Color(0xFFF8BBD0), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        color = custom_mint_green,
                        shape = RoundedCornerShape(8.dp))
                    .border(
                        color = custom_dark_green,
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Screen",
                    fontSize = 16.sp,
                    color = custom_dark_green,
                    fontWeight = FontWeight.Bold
                )
            }

            // Sơ đồ ghế
            SeatMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SeatLegend(color = Color.Gray, label = "Booked")
                SeatLegend(color = custom_dark_green, label = "Selecting")
                SeatLegend(color = Color(0xFFE0E0E0), label = "Available")
                SeatLegend(color = custom_light_red, label = "Double seat")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "2 adult tickets",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = movieTitle,
                        fontSize = 18.sp,
                        color = Color(0xFFD81B60)
                    )
                    Text(
                        text = "Ghế: E5, E6",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "130.000 đ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD81B60)
                )
            }

            Button(
                onClick = {
                    // TODO: PAYMENT
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = custom_light_green2),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Pay",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SeatMap(modifier: Modifier = Modifier) {
    val rows = listOf("A", "B", "C", "D", "E", "F", "G")
    val seatsPerRow = 8
    val selectedSeats = remember { mutableStateListOf<String>() }
    val bookedSeats = listOf("A1", "A2", "B3", "C5") // Ghế đã đặt
    val coupleSeats = listOf("G1-G2", "G3-G4", "G5-G6", "G7-G8") // Ghế đôi

    Column(modifier = modifier) {
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = row,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .width(20.dp)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
                for (seatNum in 1..seatsPerRow) {
                    val seatId = "$row$seatNum"
                    val isCoupleSeat = coupleSeats.any { it.contains(seatId) }
                    val coupleSeatPair =
                        if (isCoupleSeat) coupleSeats.find { it.contains(seatId) } else null
                    val isBooked = bookedSeats.contains(seatId)
                    val isSelected =
                        selectedSeats.contains(seatId) || (coupleSeatPair != null && selectedSeats.any { it in coupleSeatPair })

                    if (isCoupleSeat && seatNum % 2 == 1) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(30.dp)
                                .background(
                                    when {
                                        isBooked -> Color.Gray
                                        isSelected -> custom_dark_green
                                        else -> custom_light_red
                                    },
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable(enabled = !isBooked) {
                                    if (isSelected) {
                                        selectedSeats.removeAll { it in coupleSeatPair.orEmpty() }
                                    } else {
                                        coupleSeatPair?.split("-")
                                            ?.forEach { selectedSeats.add(it) }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = coupleSeatPair?.replace("-", " ") ?: "",
                                fontSize = 12.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else if (!isCoupleSeat || seatNum % 2 == 0) { // Ghế đơn hoặc bỏ qua ghế thứ 2 của ghế đôi
                        if (!isCoupleSeat) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(
                                        when {
                                            isBooked -> Color.Gray
                                            isSelected -> custom_dark_green
                                            else -> Color(0xFFE0E0E0)
                                        },
                                        RoundedCornerShape(4.dp)
                                    )
                                    .clickable(enabled = !isBooked) {
                                        if (isSelected) {
                                            selectedSeats.remove(seatId)
                                        } else {
                                            selectedSeats.add(seatId)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = seatNum.toString(),
                                    fontSize = 12.sp,
                                    color = if (isSelected || isBooked) Color.White else Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeatLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SeatSelectionScreenPreview() {
    val navController = rememberNavController()
    SeatSelectionScreen(
        theaterName = "CGV Vivo City",
        showTime = "Thứ 6, 23/05 - 17:50",
        movieTitle = "Doraemon Movie 44: Nobita và....",
        navController = navController
    )
}