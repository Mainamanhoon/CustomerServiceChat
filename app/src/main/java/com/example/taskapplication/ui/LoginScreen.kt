package com.example.taskapplication.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.taskapplication.data.Resource
import com.example.taskapplication.viewModels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val loginState by viewModel.loginState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.login(username, password) }) {
                Text("Login")
            }

            when (loginState) {
                is Resource.Loading -> CircularProgressIndicator()
                is Resource.Success -> {
                    val token = (loginState as Resource.Success).result
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    onLoginSuccess(token)
                }
                is Resource.Failure -> {
                    val error = (loginState as Resource.Failure).exception.message ?: "Unknown Error"
                    Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                }
                null -> Unit
            }
        }
    }
}
