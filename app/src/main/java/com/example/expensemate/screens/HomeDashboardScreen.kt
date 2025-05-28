package com.example.expensemate.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import com.airbnb.lottie.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(navController: NavHostController, viewModel: ExpenseViewModel) {

    val expenses by viewModel.expenses.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val userExpenses = expenses.filter { it.userId == userId }

    val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "User"
    val todayDate = SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(Date())
    val todayTotal = userExpenses.filter { it.date == todayDate }.sumOf { it.amount }
    var showAllRecent by remember { mutableStateOf(false) }
    val recentTransactions = if (showAllRecent) userExpenses else userExpenses.take(4)
    val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())



    LaunchedEffect(Unit) {
        viewModel.fetchDailyAdvice()
    }


    val today = sdf.parse(todayDate)
    val upcomingBills = userExpenses.filter {
        try {
            sdf.parse(it.date)?.after(today) == true
        } catch (e: Exception) {
            false
        }
    }
    var showAllBills by remember { mutableStateOf(false) }
    val upcomingBillsToShow = if (showAllBills) upcomingBills else upcomingBills.take(2)




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
                    text = "👋 Welcome, $userName!",
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
                            text = "Thought of the day : ${viewModel.dailyAdvice.value}",
                            fontSize = 16.sp,
                            color = Color.White
                        )

                    }
                }
            }

            item {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.savingmoney))
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever
                )

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .width(400.dp)
                        .padding(bottom = 24.dp)
                )
            }


            if (upcomingBills.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Upcoming Bill",
                                fontSize = 20.sp,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                items(upcomingBillsToShow) { bill ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                    ) {
                        Text(
                            text = "${bill.category}: \$${bill.amount} due on ${bill.date}",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                item {
                    if (upcomingBills.size > 2) {
                        Text(
                            text = if (showAllBills) "Show Less ▲" else "Show More ▼",
                            color = Color.Cyan,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAllBills = !showAllBills }
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

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
                        .padding(vertical = 4.dp)
                        ,
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Text(
                        text = "💰 \$${expense.amount}  📅 ${expense.date}  📂 ${expense.category}",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            item {
                Text(
                    text = if (showAllRecent) "Show Less ▲" else "Show All ▼",
                    color = Color.Cyan,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAllRecent = !showAllRecent }
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
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
