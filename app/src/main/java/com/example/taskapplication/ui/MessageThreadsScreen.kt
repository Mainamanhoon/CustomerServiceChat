package com.example.taskapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskapplication.data.Message
import com.example.taskapplication.data.Resource
import com.example.taskapplication.viewModels.MessagesViewModel

@Composable
fun MessageThreadsScreen(
    viewModel: MessagesViewModel,
    authToken: String,
    onThreadSelected: (Int) -> Unit
) {
    LaunchedEffect(authToken) {
        viewModel.fetchMessages(authToken)
    }

    val messagesState = viewModel.allMessages.collectAsState().value

    when (messagesState) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is Resource.Success -> {
            val threads = viewModel.getThreads()
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(threads) { thread ->
                    MessageThreadItem(message = thread) {
                        onThreadSelected(thread.thread_id) // Pass thread_id to the lambda
                    }
                }
            }
        }
        is Resource.Failure -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${messagesState.exception.message}")
            }
        }
        null -> {}
    }
}


@Composable
fun MessageThreadItem(message: Message, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = "Sender: ${message.user_id ?: message.agent_id}")
        Text(text = "Message: ${message.body}")
        Text(text = "Timestamp: ${message.timestamp}")
    }
}

