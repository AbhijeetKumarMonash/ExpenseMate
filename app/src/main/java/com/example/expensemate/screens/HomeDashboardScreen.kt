package com.example.expensemate.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensemate.R
import com.example.expensemate.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(navController: NavHostController) {
    val viewModel: ExpenseViewModel = viewModel()
    val expenses by viewModel.expenses.collectAsState()
    val todayDate = SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(Date())
    val todayTotal = expenses.filter { it.date == todayDate }.sumOf { it.amount }
    val recentTransactions = expenses.take(4)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFFFFD700),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFFFFD700)
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "👋 Welcome!",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Today's Expenses: \$${"%.2f".format(todayTotal)}",
                            fontSize = 20.sp,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tip: Small savings make a big difference!",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            item {
                Image(
                    painter = painterResource(id = R.drawable.savingmoney),
                    contentDescription = "Savings Illustration",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(bottom = 24.dp),
                    contentScale = ContentScale.Fit
                )
            }

            item {
                Text(
                    text = "Recent Transactions",
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            items(recentTransactions) { expense ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Text(
                        text = "${expense.category} - \$${expense.amount} (${expense.date})",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "\"Save money, and money will save you.\"",
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
