package com.example.taskapplication.service

import com.example.taskapplication.data.LoginRequest
import com.example.taskapplication.data.LoginResponse
import com.example.taskapplication.data.Message
import com.example.taskapplication.data.MessageRequest
import com.example.taskapplication.data.MessageThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    @GET("api/messages")
    suspend fun fetchMessages(@Header("X-Branch-Auth-Token") authToken: String): List<Message>

    @POST("api/messages")
    suspend fun sendMessage(
        @Header("X-Branch-Auth-Token") authToken: String,
        @Body messageRequest: MessageRequest
    ): Message

    companion object {
        private const val BASE_URL = "https://android-messaging.branch.co/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}