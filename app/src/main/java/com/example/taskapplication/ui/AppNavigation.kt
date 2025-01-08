package com.example.taskapplication.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskapplication.service.ApiService
import com.example.taskapplication.viewModels.MessagesViewModel

@Composable
fun AppNavigation(navController: NavHostController, apiService: ApiService) {
    NavHost(navController, startDestination = "threads") {
        composable("threads") {
            MessageThreadsScreen(
                viewModel = MessagesViewModel(apiService),
                authToken = "your_auth_token",
                onThreadSelected = { threadId ->
                    navController.navigate("conversation/$threadId")
                }
            )
        }
        composable("conversation/{threadId}") { backStackEntry ->
            val threadId = backStackEntry.arguments?.getString("threadId")?.toInt() ?: return@composable
            ConversationScreen(
                threadId = threadId,
                messages = listOf(), // Replace with actual message list
                onSendMessage = { message -> /* Handle sending */ }
            )
        }
    }
}
