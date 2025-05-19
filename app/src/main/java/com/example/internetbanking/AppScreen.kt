package com.example.internetbanking

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.internetbanking.ui.customer.BookHotelRoomsScreen
import com.example.internetbanking.ui.customer.BuyFlightTicketsScreen
import com.example.internetbanking.ui.customer.BuyMovieTicketsScreen
import com.example.internetbanking.ui.customer.CustomerHome
import com.example.internetbanking.ui.customer.DepositPhoneMoneyScreen
import com.example.internetbanking.ui.customer.DepositAndWithdrawScreen
import com.example.internetbanking.ui.customer.EcommerceScreen
import com.example.internetbanking.ui.customer.LocateShortestPathScreen
import com.example.internetbanking.ui.customer.LocateUserAndBankScreen
import com.example.internetbanking.ui.customer.PayBillsScreen
import com.example.internetbanking.ui.customer.ProfileScreen
import com.example.internetbanking.ui.customer.TransactionHistoryScreen
import com.example.internetbanking.ui.customer.TransferScreen
import com.example.internetbanking.ui.customer.mortgage.ViewMortgageMoneyScreen
import com.example.internetbanking.ui.customer.saving.ViewProfitsAndRatesScreen
import com.example.internetbanking.ui.officer.CreateCustomerScreen
import com.example.internetbanking.ui.officer.EditCustomerProfile
import com.example.internetbanking.ui.officer.OfficerHome
import com.example.internetbanking.ui.shared.LoginScreen
import com.example.internetbanking.viewmodels.CustomerViewModel
import com.example.internetbanking.viewmodels.LoginViewModel
import com.example.internetbanking.viewmodels.OfficerViewModel
import kotlin.math.log

enum class AppScreen() {
    Login,
    OfficerHome, CreateCustomer, EditCustomerProfile,
    CustomerHome, Profile,
    Deposit, Withdraw, TransactionHistory,
    Transfer, PayBills, DepositPhoneMoney, BuyFlightTickets, BuyMovieTickets, BookHotelRooms, Ecommerce,
    LocateUserAndBank, LocateShortestPath,
    ViewMortgageMoney,
    ViewProfitsAndRates

}

@Composable
fun AppScreen(
    loginViewModel: LoginViewModel = viewModel(),
    officerViewModel: OfficerViewModel = viewModel(),
    customerViewModel: CustomerViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.name                     // TODO: CHANGE TO login.name when done
    ) {
        // Login
        composable(route = AppScreen.Login.name) {
            LoginScreen(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }

        // Home Screen
        composable(route = AppScreen.OfficerHome.name) {
            OfficerHome(
                officerViewModel = officerViewModel,
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.CustomerHome.name) {
            CustomerHome(
                customerViewModel = customerViewModel,
                loginViewModel = loginViewModel,
                navController = navController
            )
        }


        // Officer
        composable(route = AppScreen.CreateCustomer.name) {
            CreateCustomerScreen(
                officerViewModel = officerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.EditCustomerProfile.name) {
            EditCustomerProfile(
                officerViewModel = officerViewModel,
                navController = navController
            )
        }


        // Customer - Profile
        composable(route = AppScreen.Profile.name) {
            ProfileScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }


        // Customer - Bank Account
        composable(route = AppScreen.Deposit.name) {
            DepositAndWithdrawScreen(
                customerViewModel = customerViewModel,
                userSelect = 0,
                navController = navController
            )
        }
        composable(route = AppScreen.Withdraw.name) {
            DepositAndWithdrawScreen(
                customerViewModel = customerViewModel,
                userSelect = 1,
                navController = navController
            )
        }
        composable(route = AppScreen.TransactionHistory.name) {
            TransactionHistoryScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }


        // Customer - Transfer
        composable(route = AppScreen.Transfer.name) {
            TransferScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.PayBills.name) {
            PayBillsScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.DepositPhoneMoney.name) {
            DepositPhoneMoneyScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.BuyFlightTickets.name) {
            BuyFlightTicketsScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.BuyMovieTickets.name) {
            BuyMovieTicketsScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.BookHotelRooms.name) {
            BookHotelRoomsScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.Ecommerce.name) {
            EcommerceScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }

        // Customer - Navigation
        composable(route = AppScreen.LocateUserAndBank.name) {
            LocateUserAndBankScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }
        composable(route = AppScreen.LocateShortestPath.name) {
            LocateShortestPathScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }


        // Mortgage Account Only
        composable(route = AppScreen.ViewMortgageMoney.name) {
            ViewMortgageMoneyScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }


        // Saving Account Only
        composable(route = AppScreen.ViewProfitsAndRates.name) {
            ViewProfitsAndRatesScreen(
                customerViewModel = customerViewModel,
                navController = navController
            )
        }
    }
}