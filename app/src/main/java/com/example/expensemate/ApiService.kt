package com.example.expensemate


import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Data classes for JSON parsing
data class AdviceResponse(val slip: Slip)
data class Slip(val advice: String)

interface AdviceApiService {
    @GET("advice")
    suspend fun getAdvice(): AdviceResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.adviceslip.com/"

    val api: AdviceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdviceApiService::class.java)
    }
}
