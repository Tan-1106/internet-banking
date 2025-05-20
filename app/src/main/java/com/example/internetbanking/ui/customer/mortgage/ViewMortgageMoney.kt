package com.example.internetbanking.ui.customer.mortgage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.internetbanking.ui.theme.custom_mint_green
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val DarkMintGreen = Color(0xFF2E7D32)
val LightDarkMintGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMortgageMoneyScreen(
    navController: NavHostController
) {
    // Trạng thái UI
    var loanAmount by remember { mutableStateOf(9000000.0) } // Mặc định 9 triệu
    var loanTerm by remember { mutableStateOf(9) } // Mặc định 9 tháng
    val monthlyPayment by remember(loanAmount, loanTerm) { mutableStateOf((loanAmount / loanTerm)*1.2) }
    val totalPayment by remember(monthlyPayment, loanTerm) { mutableStateOf(monthlyPayment *  loanTerm)} // Tính toán mẫu
    val dueDate by remember(loanTerm) {
        mutableStateOf(
            LocalDate.now().plusMonths(loanTerm.toLong()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )
    }
    Scaffold(
        containerColor = Color.White, // Nền trắng
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tạo khoản vay",
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
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Banner "Tạo khoản vay" với icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(
                        color = DarkMintGreen,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Bạn muốn vay",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format("%,dđ", loanAmount.toInt()),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Thanh trượt số tiền
            Slider(
                value = loanAmount.toFloat(),
                onValueChange = { loanAmount = it.toDouble().coerceIn(6000000.0, 15000000.0) },
                valueRange = 6000000f..15000000f,
                steps = 8, // 9 bước (6, 7, 8, ..., 15 triệu)
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = SliderDefaults.colors(
                    thumbColor = DarkMintGreen,
                    activeTrackColor = DarkMintGreen,
                    inactiveTrackColor = DarkMintGreen.copy(alpha = 0.3f)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("6 Triệu", color = Color.Black, fontSize = 14.sp)
                Text("15 Triệu", color = Color.Black, fontSize = 14.sp)
            }

            // Phần chọn thời hạn
            Text(
                text = "Thời hạn",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .clickable { loanTerm = 6 }
                        .padding(4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (loanTerm == 6) LightDarkMintGreen else Color.White
                    ),
                    border = BorderStroke(1.dp, if (loanTerm == 6) DarkMintGreen else Color.LightGray)
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "6 tháng",
                            fontSize = 16.sp,
                            color = if (loanTerm == 6) Color.White else Color.Black
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .clickable { loanTerm = 9 }
                        .padding(4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (loanTerm == 9) LightDarkMintGreen else Color.White
                    ),
                    border = BorderStroke(1.dp, if (loanTerm == 9) DarkMintGreen else Color.LightGray)
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "9 tháng",
                            fontSize = 16.sp,
                            color = if (loanTerm == 9) Color.White else Color.Black
                        )
                    }
                }
            }

            // Thông tin chi tiết
            Text(
                text = "Thông tin chi tiết",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Số tiền vay", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = String.format("%,dđ", loanAmount.toInt()),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Trả mỗi tháng", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = String.format("%,dđ", monthlyPayment.toInt()),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ngày hết hạn", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = dueDate,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Số tiền thực nhận", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = String.format("%,dđ", loanAmount.toInt()),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tổng thanh toán", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = String.format("%,dđ", totalPayment.toInt()),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                }
            }

            // Nút "Tạo khoản vay"
            Button(
                onClick = { /* Xử lý tạo khoản vay */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkMintGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Tạo khoản vay",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewMortgageMoneyScreenPreview() {
    val fakeNavController: NavHostController = rememberNavController()
    ViewMortgageMoneyScreen(fakeNavController)
}