package com.example.taskapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.taskapplication.service.ApiService
import com.example.taskapplication.ui.ui.theme.TaskApplicationTheme
import com.example.taskapplication.viewModels.LoginViewModel
import com.example.taskapplication.viewModels.MessagesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiService.create()

        setContent {
            TaskApplicationTheme {
                var authToken by remember { mutableStateOf<String?>(null) }
                val navController = rememberNavController()

                if (authToken == null) {
                    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory(apiService))
                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = { token -> authToken = token }
                    )
                } else {
                    val messagesViewModel: MessagesViewModel = viewModel(factory = MessagesViewModel.Factory(apiService))
                    AppNavigation(navController, messagesViewModel, authToken!!)
                }
            }
        }
    }
}
