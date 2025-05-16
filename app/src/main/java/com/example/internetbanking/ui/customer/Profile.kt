package com.example.internetbanking.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.R
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_dark_green
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    val customerUiState by customerViewModel.uiState.collectAsState()

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
                            text = "Your Information",
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

                                }
                        )
                    },
                    actions = {
                        Icon(
                            imageVector = Icons.Filled.EditNote,
                            contentDescription = "Edit Information",
                            tint = custom_mint_green,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {

                                }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .background(
                            brush = GradientColors.Green_Ripple
                        )
                )
            },
            modifier = Modifier.systemBarsPadding()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text(
                    "Your Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = custom_dark_green,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                LazyColumn {
                    item {
                        InformationLine(
                            label = "Full name",
                            placeholder = customerUiState.customer.account.fullName,
                            value = customerUiState.customer.account.fullName,
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item{
                        InformationLine(
                            label = "Gender",
                            placeholder = customerUiState.customer.account.gender,
                            value = customerUiState.customer.account.gender,
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        InformationLine(
                            label = "Identification number",
                            placeholder = customerUiState.customer.account.identificationNumber,
                            value = customerUiState.customer.account.identificationNumber,
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        InformationLine(
                            label = "Phone number",
                            placeholder = customerUiState.customer.account.phoneNumber,
                            value = customerUiState.customer.account.phoneNumber,
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        InformationLine(
                            label = "Email",
                            placeholder = customerUiState.customer.account.email,
                            value = customerUiState.customer.account.email,
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        InformationLine(
                            label = "Birthday",
                            placeholder = customerUiState.customer.account.birthday,
                            value = customerUiState.customer.account.birthday,
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        InformationLine(
                            label = "Address",
                            placeholder = customerUiState.customer.account.address,
                            value = customerUiState.customer.account.address,
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                    item {
                        InformationLine(
                            label = "Role",
                            placeholder = customerUiState.customer.account.role,
                            value = customerUiState.customer.account.role,
                            onValueChange = {},
                            isEnable = false
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ProfileScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    ProfileScreen(fakeViewModel, fakeNavController)
}