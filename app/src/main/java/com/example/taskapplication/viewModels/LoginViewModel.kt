package com.example.taskapplication.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapplication.data.LoginRequest
import com.example.taskapplication.data.Resource
import com.example.taskapplication.service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val apiService: ApiService) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<String>?>(null)
    val loginState :StateFlow<Resource<String>?> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            try {
                val response = apiService.login(LoginRequest(username, password))
                _loginState.value = Resource.Success(response.auth_token)
            } catch (e: Exception) {
                _loginState.value = Resource.Failure(e)
            }
        }
    }
}
