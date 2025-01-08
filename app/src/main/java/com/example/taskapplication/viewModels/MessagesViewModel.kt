package com.example.taskapplication.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapplication.data.Message
import com.example.taskapplication.data.MessageRequest
import com.example.taskapplication.data.Resource
import com.example.taskapplication.service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessagesViewModel(private val apiService: ApiService) : ViewModel() {
    private val _allMessages = MutableStateFlow<Resource<List<Message>>>(Resource.Loading)
    private val _threadMessages = MutableStateFlow<List<Message>>(emptyList())

    val allMessages: StateFlow<Resource<List<Message>>> = _allMessages
    val threadMessages: StateFlow<List<Message>> = _threadMessages

    fun fetchMessages(authToken: String) {
        Log.d("MessagesViewModel", "Fetch Api is aclled")
        viewModelScope.launch {
            _allMessages.value = Resource.Loading
            try {
                val messages = apiService.fetchMessages(authToken)
                _allMessages.value = Resource.Success(messages)
            } catch (e: Exception) {
                _allMessages.value = Resource.Failure(e)
            }
        }
    }



    fun loadThreadMessages(threadId: Int) {
        val messages = (_allMessages.value as? Resource.Success)?.result ?: return
        _threadMessages.value = messages.filter { it.thread_id == threadId }
    }


    fun sendMessage(authToken: String, threadId: Int, messageBody: String) {
        viewModelScope.launch {
            try {
                // Create the message request and send the message
                val messageRequest = MessageRequest(thread_id = threadId, body = messageBody)
                val newMessage = apiService.sendMessage(authToken, messageRequest)

                // Add the new message to the current thread's messages locally
                val updatedThreadMessages = _threadMessages.value.toMutableList()
                updatedThreadMessages.add(newMessage)
                _threadMessages.value = updatedThreadMessages

            } catch (e: Exception) {
                // Handle error (e.g., show a toast or log the exception)
                e.printStackTrace()
            }
        }
    }

    fun getThreads(): List<Message> {
        val messages = (_allMessages.value as? Resource.Success)?.result ?: return emptyList()
        return messages.groupBy { it.thread_id } // Group messages by thread_id
            .mapNotNull { (_, threadMessages) ->
                threadMessages.maxByOrNull { it.timestamp } // Get the latest message in each thread
            }
    }

}
