package com.example.internetbanking.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.ui.shared.DatePicker
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    customerViewModel: CustomerViewModel, navController: NavHostController
) {
    Scaffold(
        topBar = { TopAppBar(
            navigationIcon = { IconButton(
                onClick = {},
                content = {Icon(Icons.Default.ArrowBack,contentDescription = null)}
            ) },
            title = {Text("Transaction History")}
        ) },
        modifier = Modifier.systemBarsPadding()) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    DatePicker(
                        label = "From",
                        placeholder = "05/04/2025",
                        onDatePick = {},
                        suffix = {
                            Icon(Icons.Default.CalendarToday, contentDescription = null)
                        })
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    DatePicker(label = "To", placeholder = "05/04/2025", onDatePick = {}, suffix = {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                    })
                }
            }

            Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                ElevatedButton(
                    shape = RoundedCornerShape(percent = 20),
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    )
                ) {
                    Text("Search")
                }
            }
            Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Transaction History", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .background(
                        Color.Gray, shape = RoundedCornerShape(percent = 20)
                    )
                    .padding(5.dp), contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {}, modifier = Modifier
                            .weight(0.3f)
                            .background(
                                Color.Green, shape = RoundedCornerShape(percent = 20)
                            )
                            .padding(vertical = 10.dp), contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "All",
                        )
                    }
                    Spacer(Modifier.width(5.dp))
                    TextButton(
                        onClick = {}, modifier = Modifier
                            .weight(0.3f)
                            .background(
                                Color.Green, shape = RoundedCornerShape(percent = 20)
                            )
                            .padding(vertical = 10.dp), contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "In",
                        )
                    }
                    Spacer(Modifier.width(5.dp))
                    TextButton(
                        onClick = {}, modifier = Modifier
                            .weight(0.3f)
                            .background(
                                Color.Green, shape = RoundedCornerShape(percent = 20)
                            )
                            .padding(vertical = 10.dp), contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "Out",
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f)

            ) {
                items(count = 10) {
                    Row (modifier = Modifier.padding(5.dp)){
                        Column {
                            Text("04/05/2025", fontSize = 15.sp, color = Color.Gray)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(6f)) {
                                    Text(
                                        "IDeqcbjkamdncbzikjcnacbasjkc,nabkscjkn cjadb",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(Modifier.width(5.dp))
                                Column(
                                    modifier = Modifier.weight(3f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) { Text("-12.000VND") }
                                Spacer(Modifier.width(5.dp))
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.ArrowRight,
                                        contentDescription = null
                                    )
                                }
                            }
                            Spacer(Modifier.height(5.dp))
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun TransactionHistoryScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    TransactionHistoryScreen(fakeViewModel, fakeNavController)
}
