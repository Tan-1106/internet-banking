package com.example.internetbanking.ui.officer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.AppScreen
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.GradientBackground
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.OfficerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficerHome(
    officerViewModel: OfficerViewModel,
    navController: NavHostController,
) {
    val officerUiState by officerViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = custom_mint_green,
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_2_mint_green),
                        contentDescription = "LB Digital",
                        modifier = Modifier.width(100.dp)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { //TODO: LogOut

                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = custom_mint_green
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .background(
                        brush = GradientColors.GreenRipple
                    )
            )
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            GradientBackground(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 36.dp)
                    .fillMaxHeight(0.125f)
                    .clip(
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Officer:",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.width(100.dp)
                        )
                        Text(
                            text = officerUiState.officer.fullName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Account ID:",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.width(100.dp)
                        )
                        Text(
                            text = officerUiState.officer.userId,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = GradientColors.GreenDarkToLight
                    )
            ) {
                Text(
                    text = "FEATURES",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(150.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.create_customer),
                            contentDescription = "Create customer",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    navController.navigate(route = AppScreen.CreateCustomer.name)
                                }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Create Customer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = custom_dark_green
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(150.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.edit_customer),
                            contentDescription = "Edit customer",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    navController.navigate(route = AppScreen.EditCustomerProfile.name)
                                }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Edit Customer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = custom_dark_green
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(R.drawable.edit_rates),
                        contentDescription = "Edit Profitable rates",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                navController.navigate(route = AppScreen.ModifyProfitableRates)
                            }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Edit Profitable Rates",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = custom_dark_green
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun OfficerHomePreview(){
    val fakeViewModel: OfficerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    OfficerHome(fakeViewModel, fakeNavController)
}