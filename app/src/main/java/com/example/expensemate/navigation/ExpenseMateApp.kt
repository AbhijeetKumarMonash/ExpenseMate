package com.example.expensemate.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensemate.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseMateApp() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "login") {
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
            composable("home") { HomeDashboardScreen(navController) }
            composable("add_expense") { AddExpenseScreen(navController) }
            composable("report") { ReportScreen(navController) }
            composable("chatbot") { ChatbotScreen(navController) }
            composable("signup") { SignupScreen(navController) }
            composable("profile") { ProfileScreen(navController) }

        }
    }
}
