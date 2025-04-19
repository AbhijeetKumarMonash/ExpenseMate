package com.example.expensemate.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chatbot Assistant",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Welcome Message
                Text(
                    text = "Ask ExpenseMate your money-related questions!",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                // Fake User Message
                Surface(
                    color = Color(0xFFFFD700),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "How can I save money on groceries?",
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Fake Bot Response
                Surface(
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text(
                        text = "Try meal planning and buying in bulk. 🍎",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Another User Message
                Surface(
                    color = Color(0xFFFFD700),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Suggest a budgeting method!",
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Another Bot Response
                Surface(
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text(
                        text = "The 50/30/20 rule is a great starting point! 💡",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Fake Input Box at bottom
            TextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        "Type your question...",
                        color = Color.Gray
                    )
                },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.DarkGray,
                    disabledTextColor = Color.LightGray,
                    disabledIndicatorColor = Color.Gray,
                    disabledPlaceholderColor = Color.Gray
                )
            )
        }
    }
}
