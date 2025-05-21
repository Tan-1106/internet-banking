package com.example.internetbanking.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.AppScreen
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.formatCurrencyVN
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositAndWithdrawScreen(
    customerViewModel: CustomerViewModel,
//    customerViewModelDT: CustomerViewModelDT,
    userSelect: Int = 0,
    navController: NavHostController
) {
    var selectedTabIndex by remember { mutableIntStateOf(userSelect) }
    val tabs = listOf("Deposit", "Withdraw")
    var amount by remember { mutableStateOf(BigDecimal.ZERO) }

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
            topBar = {

                TopAppBar(
                    title = {
                        Text(
                            text = "Deposit/Withdraw",
                            color = custom_mint_green
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.navigateUp() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = custom_mint_green
                            )
                        }
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
                    if (selectedTabIndex == 0) {

                        ElevatedButton(
                            onClick = {
                                customerViewModel.onStartDeposit(
                                    amount = amount,
                                    navController = navController
                                )
                            },
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 10.dp)
                                .fillMaxSize(),
                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = custom_light_green1
                            )
                        ) {
                            Text(
                                text = "Continue",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        ElevatedButton(
                            onClick = {},
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 10.dp)
                                .fillMaxSize(),
                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = custom_light_green1
                            )
                        ) {
                            Text(
                                text = "Withdraw",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) { paddingValue ->
            Column(
                modifier = Modifier
                    .padding(paddingValue)
                    .padding(10.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    contentColor = Color.Black,
                    indicator = { tabPositions ->
                        TabRowDefaults
                            .SecondaryIndicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = custom_light_green1
                            )
                    },
                    containerColor = Color.Transparent
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Row {
                                    Icon(
                                        if (index == 0) Icons.AutoMirrored.Filled.Input else Icons.Default.Output,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = title,
                                        fontSize = 16.sp,
                                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            selectedContentColor = if (selectedTabIndex == index) custom_light_green1 else Color.Gray,
                            modifier = Modifier.background(
                                color = if (selectedTabIndex == index) custom_mint_green else Color.White,
                                shape = RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> TabScreen(
                        amount = amount,
                        label = "Enter the amount to deposit",
                        onAmountChange = {
                            amount = if (it.isEmpty()) {
                                BigDecimal.ZERO
                            } else {
                                it.toBigDecimal()
                            }
                        }
                    )

                    1 -> TabScreen(
                        amount = amount,
                        label = "Enter the amount to withdraw",
                        onAmountChange = {
                            amount = if (it.isEmpty()) {
                                BigDecimal.ZERO
                            } else {
                                it.toBigDecimal()
                            }
                        }
                    )
                }
                Spacer(Modifier.height(10.dp))
                HintAmount(
                    onAmountClick = { amount = it }
                )
            }
        }
    }
}


@Composable
fun HintAmount(
    onAmountClick: (BigDecimal) -> Unit
) {
    val amounts = listOf(
        BigDecimal("50000"),
        BigDecimal("100000"),
        BigDecimal("200000"),
        BigDecimal("500000"),
        BigDecimal("1000000"),
        BigDecimal("2000000")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    corner = CornerSize(20.dp)
                )
            )
            .padding(10.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {

            items(items = amounts) { amount ->
                OutlinedButton(
                    onClick = { onAmountClick(amount) },
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    contentPadding = PaddingValues(all = 0.dp),
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text("${formatCurrencyVN(amount)}đ", fontSize = 15.sp, color = Color.Black)
                }
            }
        }

    }
}

@Composable
fun TabScreen(
    amount: BigDecimal,
    onAmountChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp,
                    topStart = 0.dp,
                    topEnd = 0.dp
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(label, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = "${formatCurrencyVN(amount)}đ",
                onValueChange = {
                    val raw = it.replace(".", "").replace("đ", "").trim()
                    onAmountChange(raw)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = OutlinedTextFieldDefaults.shape,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = custom_mint_green,
                    focusedLabelColor = custom_light_green1,
                ),
                placeholder = { Text("0đ") }, modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}


//@Preview(
//    showBackground = true
//)
//@Composable
//fun DepositScreenPreview() {
//    val fakeViewModel: CustomerViewModel = viewModel()
//    val fakeNavController: NavHostController = rememberNavController()
//
//    DepositAndWithdrawScreen(fakeViewModel, 0, fakeNavController)
//}