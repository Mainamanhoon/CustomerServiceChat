package com.example.taskapplication.data

data class Message(
    val id: Int,
    val thread_id: Int,
    val body: String,
    val timestamp: String,
    val user_id: String?,
    val agent_id: String?
)

