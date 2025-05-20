package com.example.internetbanking.ui.customer.mortgage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMortgageMoneyScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
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
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Installment",
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
            ) {

            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ViewMortgageMoneyScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    ViewMortgageMoneyScreen(fakeViewModel, fakeNavController)
}