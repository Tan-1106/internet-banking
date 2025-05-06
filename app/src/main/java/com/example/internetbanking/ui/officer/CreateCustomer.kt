package com.example.internetbanking.ui.officer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.ui.shared.DatePicker
import com.example.internetbanking.ui.shared.InformationSelect
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.OfficerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCustomerScreen(
    officerViewModel: OfficerViewModel,
    navController: NavHostController
) {
    var userId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var identificationNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    
    Scaffold(
        containerColor = custom_mint_green,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create customer",
                        fontWeight = FontWeight.Bold,
                        color = custom_mint_green
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back",
                            tint = custom_mint_green,
                            modifier = Modifier.size(30.dp)
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
                .padding(20.dp)
        ) {
            Text(
                "Customer Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            DatePicker(
                label = "Birthday",
                placeholder = "Select birthday",
                onDatePick = { birthday = it },
                suffix = {
                    VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select birthday")
                }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CreateCustomerScreenPreview(){
    val fakeViewModel: OfficerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    CreateCustomerScreen(fakeViewModel, fakeNavController)
}