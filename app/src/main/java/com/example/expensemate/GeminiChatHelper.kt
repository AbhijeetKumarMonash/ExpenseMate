package com.example.expensemate

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

object GeminiChatHelper {

    private const val API_KEY = "AIzaSyDYfliNN9R7sFJnbI5f93vd9de3xgAAtww"

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = API_KEY
    )

    suspend fun getChatResponse(prompt: String): String {
        return try {
            val contextPrefix = """
Act as a friendly financial literacy coach. 
You don’t give personal investment advice, but you help users understand budgeting, saving, and smart money habits. 
Keep it concise and supportive.
""".trimIndent()

            val response = model.generateContent(
                content {
                    text("$contextPrefix\n\nUser: $prompt")
                }
            )

            response.text ?: "No reply."
        } catch (e: Exception) {
            "⚠️ ${e.localizedMessage ?: "Error"}"
        }
    }
}
