package com.example.expensemate.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensemate.MarkdownTextCompat
import com.example.expensemate.viewmodel.ChatViewModel


import com.example.expensemate.Message


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(navController: NavHostController, viewModel: ChatViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chatbot Assistant",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Black)
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

            val samplePrompts = listOf(
                "How can I save money on groceries?",
                "What is the 50/30/20 budgeting rule?",
                "Suggest a daily habit to improve my savings.",
                "How do I track monthly expenses?",
                "What are ways to reduce unnecessary spending?"
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Suggested Questions",
                    color = Color(0xFFFFD700),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(samplePrompts.size) { index ->
                        AssistChip(
                            onClick = { viewModel.userInput = samplePrompts[index] },
                            label = { Text(samplePrompts[index], fontSize = 12.sp) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = Color(0xFF424242),
                                labelColor = Color.White
                            )
                        )
                    }
                }
            }


            // ✅ Chat Messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                itemsIndexed(viewModel.chatHistory.toList()) { _, msg ->
                    val isUser = msg.role == "user"
                    Surface(
                        color = if (isUser) Color(0xFF424242) else Color(0xFF424242),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(0.85f)
                            .align(if (isUser) Alignment.End else Alignment.Start)
                    ) {
                        MarkdownTextCompat(
                            markdown = msg.content,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            if (viewModel.isTyping) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.Start)
                ) {
                    Surface(
                        color = Color(0xFF2E2E2E),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Assistant is thinking...",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }


            // ✅ Input Box
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                TextField(
                    value = viewModel.userInput,
                    onValueChange = { viewModel.userInput = it },
                    placeholder = { Text("Ask something...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.LightGray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )

                )
                Button(onClick = { viewModel.sendChatMessage() },
                    modifier = Modifier.height(56.dp)) {
                    Text("Send")
                }
            }
        }
    }
}

