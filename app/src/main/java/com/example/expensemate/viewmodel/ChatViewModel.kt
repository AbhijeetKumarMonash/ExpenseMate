package com.example.expensemate.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemate.GeminiChatHelper
import com.example.expensemate.Message
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    var userInput by mutableStateOf("")
    var chatHistory = mutableStateListOf<Message>()
    var isTyping by mutableStateOf(false)

    fun sendChatMessage() {
        if (userInput.isBlank()) return

        val input = userInput
        chatHistory.add(Message("user", input))
        userInput = ""
        isTyping = true

        viewModelScope.launch {
            val reply = GeminiChatHelper.getChatResponse(input)
            chatHistory.add(Message("assistant", reply))
            isTyping = false
        }
    }
}
