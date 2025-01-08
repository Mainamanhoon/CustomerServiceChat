package com.example.taskapplication.data

data class MessageThread(
    val thread_id: String,
    val body: String,
    val timestamp: String,
    val user_id: String?,
    val agent_id: String?
)