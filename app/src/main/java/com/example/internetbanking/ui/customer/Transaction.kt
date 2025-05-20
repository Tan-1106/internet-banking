package com.example.internetbanking.ui.customer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
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

// Định nghĩa màu xanh đậm
val DarkMintGreen = Color(0xFF2E7D32) // Xanh lá đậm
val LightDarkMintGreen = Color(0xFF4CAF50) // Xanh lá nhạt hơn nhưng vẫn đậm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    navController: NavHostController
) {
    // Trạng thái UI
    var amount by remember { mutableStateOf(50000.0) } // Số tiền mặc định 50,000đ
    var cardNumber by remember { mutableStateOf("") } // Số tài khoản/thẻ
    var bankName by remember { mutableStateOf("") } // Tên ngân hàng
    var cardHolderName by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thanh toán an toàn",
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
            Text(
                text = "Tài khoản/Thẻ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            // Các trường nhập liệu
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Số tài khoản/thẻ") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = TextFieldDefaults.colors( // Thay thế bằng colors()
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = DarkMintGreen,
                    unfocusedLabelColor = Color.Black,
                    focusedIndicatorColor = DarkMintGreen,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it },
                label = { Text("Tên ngân hàng") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = TextFieldDefaults.colors( // Thay thế bằng colors()
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = DarkMintGreen,
                    unfocusedLabelColor = Color.Black,
                    focusedIndicatorColor = DarkMintGreen,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            OutlinedTextField(
                value = cardHolderName,
                onValueChange = { cardHolderName = it },
                label = { Text("Tên chủ thẻ") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = TextFieldDefaults.colors( // Thay thế bằng colors()
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = DarkMintGreen,
                    unfocusedLabelColor = Color.Black,
                    focusedIndicatorColor = DarkMintGreen,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            // Thông tin giao dịch
            Text(
                text = "Chi tiết giao dịch",
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
                        Text("Dịch vụ", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = "Nạp tiền vào Ví MoMo",
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
                        Text("Ngân hàng", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = if (bankName.isNotEmpty()) bankName else "Techcombank",
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
                        Text("Số tiền", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = String.format("%,dđ", amount.toInt()),
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
                        Text("Phí giao dịch", fontSize = 16.sp, color = Color.Black)
                        Text(
                            text = "Miễn phí",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMintGreen
                        )
                    }
                }
            }

            // Tổng tiền
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tổng tiền", fontSize = 16.sp, color = Color.Black)
                Text(
                    text = String.format("%,dđ", amount.toInt()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkMintGreen
                )
            }

            // Nút Xác nhận
            Button(
                onClick = { /* Xử lý xác nhận */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 16.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkMintGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Xác nhận",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    val fakeNavController: NavHostController = rememberNavController()
    TransactionScreen(fakeNavController)
}