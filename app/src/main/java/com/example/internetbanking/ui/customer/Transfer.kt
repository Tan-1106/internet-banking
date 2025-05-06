package com.example.internetbanking.ui.customer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.internetbanking.ui.shared.InformationLine
import com.example.internetbanking.viewmodels.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    customerViewModel: CustomerViewModel,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.transfer_title)) },
                navigationIcon = { Icon(Icons.Default.ArrowBack, contentDescription = null) })
        }, modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) { paddingValue ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()

        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                BalanceInformation()
                Text(
                    "Thông tin người nhận",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                InformationLine(
                    label = "Ngân hàng nhận",
                    placeholder = "Chọn ngân hàng nhận",
                    suffix = {
                        VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    })
                InformationLine(
                    label = "Tài khoản/thẻ nhận",
                    placeholder = "Chọn hoặc nhập số tài khoản/số thẻ nhận",
                    suffix = {
                        VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                        Icon(Icons.Default.Bookmark, contentDescription = null)
                    })
                Text(
                    "Thông tin giao dịch",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                InformationLine(label = "Số tiền", placeholder = "Nhập số tiền", suffix = {
                    VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                    Text("VND", fontSize = 10.sp, color = Color.Gray)
                })
                InformationLine(label = "Nội dung", placeholder = "Nhập nội dung chuyển khoản")
                InformationLine(
                    label = "Phân loại giao dịch",
                    placeholder = "Chọn giao dịch theo mục đích",
                    suffix = {
                        VerticalDivider(modifier = Modifier.fillMaxHeight(0.8f), color = Color.Gray)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    })

            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TransferScreenPreview() {
    val fakeViewModel: CustomerViewModel = viewModel()
    val fakeNavController: NavHostController = rememberNavController()

    TransferScreen(fakeViewModel, fakeNavController)
}