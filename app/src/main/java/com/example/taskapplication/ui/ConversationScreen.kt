package com.example.taskapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskapplication.data.Message

@Composable
fun ConversationScreen(
    threadId: Int,
    messages: List<Message>,
    onSendMessage: (String) -> Unit
) {
    var newMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true // Show the latest messages at the bottom
        ) {
            items(messages.sortedByDescending { it.timestamp }) { message ->
                MessageItem(message = message)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                label = { Text("Type your reply...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newMessage.isNotBlank()) {
                    onSendMessage(newMessage)
                    newMessage = "" // Clear the input
                }
            }) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    // Determine the sender
    val sender = message.user_id ?: message.agent_id ?: "Unknown"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = "Sender: $sender", style = MaterialTheme.typography.bodySmall)
        Text(text = "Message: ${message.body}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Timestamp: ${message.timestamp}", style = MaterialTheme.typography.bodySmall)
    }
}
