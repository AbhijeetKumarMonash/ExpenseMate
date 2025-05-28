package com.example.expensemate.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.expensemate.screens.*
import com.example.expensemate.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseMateApp(viewModel: ExpenseViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "login" && currentRoute != "signup") {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") { LoginScreen(navController) }
            composable("home") { HomeDashboardScreen(navController, viewModel) }
            composable("add_expense") { AddExpenseScreen(navController, viewModel) }
            composable("report") { ReportScreen(navController) }
            composable("chatbot") { ChatbotScreen(navController) }
            composable("signup") { SignupScreen(navController) }
            composable("profile") { ProfileScreen(navController) }

        }
    }
}
