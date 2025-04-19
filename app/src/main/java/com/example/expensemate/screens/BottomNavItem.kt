package com.example.expensemate.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object AddExpense : BottomNavItem("add_expense", "Add", Icons.Filled.Add)
    object Report : BottomNavItem("report", "Report", Icons.Filled.Favorite)
    object Chatbot : BottomNavItem("chatbot", "Chatbot", Icons.Filled.Person)
}
