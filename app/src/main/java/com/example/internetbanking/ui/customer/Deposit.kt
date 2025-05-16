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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Input
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
import com.example.internetbanking.R
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_light_green1
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Deposit", "Withdraw")

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
                    title = { Text("Deposit/Withdraw", color = custom_mint_green) },
                    navigationIcon = {

                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Default.ArrowBack, contentDescription = null, tint =
                                    custom_mint_green
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .background(
                            brush = GradientColors.Green_DarkToLight
                        ),
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
                            onClick = {},
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 10.dp)
                                .fillMaxSize(),
                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = custom_light_green1
                            )
                        ) {
                            Text("Deposit", color = Color.White, fontWeight = FontWeight.Bold)
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
                            Text("Withdraw", color = Color.White, fontWeight = FontWeight.Bold)
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
                            .Indicator(
                                color = custom_light_green1,
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
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
                                        if (index == 0) Icons.Default.Input else Icons.Default.Output,
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
                    0 -> TabScreen(label = "Enter the amount to deposit")
                    1 -> TabScreen(label = "Enter the amount to withdraw")
                }
                Spacer(Modifier.height(10.dp))
                HintAmount()

            }
        }
    }
}

@Composable
fun HintAmount() {
    val amounts = listOf(
        "50.0000đ",
        "100.0000đ",
        "200.0000đ",
        "500.0000đ",
        "1.000.0000đ",
        "2.000.0000đ",
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
                    onClick = {},
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    contentPadding = PaddingValues(all = 0.dp),
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(amount, fontSize = 15.sp, color = Color.Black)
                }
            }
        }

    }
}

@Composable
fun TabScreen(label: String, modifier: Modifier = Modifier) {
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
                "",
                onValueChange = {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = OutlinedTextFieldDefaults.shape,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = custom_mint_green,
                    focusedLabelColor = custom_light_green1,
                ),
                placeholder = { Text("0đ") }

            )
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
fun DepositScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    DepositScreen(fakeViewModel, fakeNavController)
}