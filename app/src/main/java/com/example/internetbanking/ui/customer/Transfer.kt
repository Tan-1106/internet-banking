package com.example.internetbanking.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.BalanceInformation
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    Scaffold(
        containerColor = custom_mint_green,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.transfer_title),
                        fontWeight = FontWeight.Bold,
                        color = custom_mint_green
                    )
                },
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Back",
                        tint = custom_mint_green,
                        modifier = Modifier.size(30.dp)
                    )
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
    ) { paddingValue ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(paddingValue)
                .padding(vertical = 20.dp)
                .fillMaxSize()

        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                BalanceInformation()
                Text(
                    "Beneficiary Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                InformationSelect(
                    label = "Beneficiary bank",
                    placeholder = "Select beneficiary bank",
                    suffix = {
                        VerticalDivider(
                            modifier = Modifier
                                .fillMaxHeight(0.8f),
                            color = Color.Gray
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Beneficiary Bank")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                InformationSelect(
                    label = "Beneficiary account/card",
                    placeholder = "Account/card number",
                    suffix = {
                        VerticalDivider(
                            modifier = Modifier
                                .fillMaxHeight(0.8f),
                            color = Color.Gray
                        )
                        Icon(Icons.Default.Bookmark, contentDescription = "Beneficiary account/card")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Transaction Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                InformationSelect(
                    label = "Amount",
                    placeholder = "Enter amount",
                    suffix = {
                        VerticalDivider(
                            modifier = Modifier
                                .fillMaxHeight(0.8f),
                            color = Color.Gray
                        )
                        Text(
                            text = "VND",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                InformationSelect(
                    label = "Content",
                    placeholder = "Enter transaction content",
                    modifier = Modifier.fillMaxWidth()
                )
                InformationSelect(
                    label = "Transaction category",
                    placeholder = "Select by purpose",
                    suffix = {
                        VerticalDivider(
                            modifier = Modifier
                                .fillMaxHeight(0.8f),
                            color = Color.Gray
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Transaction Category")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun TransferScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    TransferScreen(fakeViewModel, fakeNavController)
}