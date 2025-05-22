package com.example.internetbanking.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.ui.theme.GradientColors
import com.example.internetbanking.ui.theme.custom_mint_green
import com.example.internetbanking.viewmodels.CustomerViewModel

data class Movie(
    val title: String,
    val duration: String,
    val genre: String,
    val rating: Float,
    val ageRating: String,
    val imageUrl: String
)

data class Theater(
    val name: String,
    val location: String,
    val distance: String,
    val address: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyMovieTicketsScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    // Dữ liệu mẫu
    val movies = listOf(
        Movie("Tham Tử Kiên: Kỳ Án Không Dầu", "2h 16m", "Hành Động", 9.5f, "16+", ""),
        Movie("Mì Tôm", "1h 50m", "Hoạt Hình", 8.0f, "P", ""),
        Movie("Mission: Impossible 5", "2h 28m", "Hành Động", 7.5f, "18+", "")
    )

    val theaters = listOf(
        Theater(
            "CGV Vivo City",
            "Ho Chi Minh City",
            "0.6 km",
            "Tầng 5 | Trung tâm thương mại SC Vivo City..."
        ),
        Theater(
            "Lotte Nam Sài Gòn",
            "Ho Chi Minh City",
            "0.6 km",
            "Tầng 3, TTTM Lotte, số 469 đường Nguyễn..."
        ),
        Theater(
            "CGV Crescent Mall",
            "Ho Chi Minh City",
            "2.1 km",
            "Tầng 5 | Crescent Mall, số 101 đường Tôn..."
        ),
        Theater(
            "Lotte Gold View",
            "Ho Chi Minh City",
            "2.6 km",
            "Tầng 3 TTTM TNL Plaza, Số 346 Đường..."
        ),
        Theater(
            "Galaxy Parc Mall Q8",
            "Ho Chi Minh City",
            "2.6 km",
            "Tầng 4 TTTM Parc Mall, 547-549 Tạ Q..."
        )
    )

    // UI State
    var searchQuery by remember { mutableStateOf("") }
    var filteredMovies by remember { mutableStateOf(movies) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var selectedTheater by remember { mutableStateOf<Theater?>(null) }

    // Search
    LaunchedEffect(searchQuery) {
        filteredMovies = if (searchQuery.isEmpty()) movies else movies.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.genre.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Buy Ticket",
                        fontSize = 18.sp,
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
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Find movie or theater name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                )

                // Danh sách phim cuộn ngang
                Text(
                    text = "Phim",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(300.dp)
                ) {
                    items(filteredMovies) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { selectedMovie = movie }
                        )
                    }
                }
            }

            item {
                // Danh sách rạp
                Text(
                    text = "Rạp đề xuất (${theaters.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LazyColumn(
                    modifier = Modifier.height(400.dp) // Đặt chiều cao cố định để tránh lỗi render
                ) {
                    items(theaters) { theater ->
                        TheaterItem(
                            theater = theater,
                            onClick = { selectedTheater = theater }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Gray) // Placeholder for image
            )
            Text(
                text = movie.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Text(
                text = "★ ${movie.rating}/10",
                fontSize = 12.sp,
                color = Color.Red
            )
            Text(
                text = movie.ageRating,
                fontSize = 12.sp,
                color = if (movie.ageRating == "18+") Color.Red else Color.Green
            )
            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Mua Ngay", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun TheaterItem(
    theater: Theater,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray) // Placeholder for logo
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = theater.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Bán ở gần rạp nay • ${theater.distance}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = theater.address,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Select Theater",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BuyMovieTicketsScreenPreview() {
    MaterialTheme { // Bọc trong MaterialTheme để render đúng
        val fakeViewModel: CustomerViewModel = viewModel()
        val fakeNavController: NavHostController = rememberNavController()
        BuyMovieTicketsScreen(fakeViewModel, fakeNavController)
    }
}