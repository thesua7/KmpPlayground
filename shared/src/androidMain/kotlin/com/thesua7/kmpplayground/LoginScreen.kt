package com.thesua7.kmpplayground


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thesua7.kmpplayground.viewmodel.LoginViewmodel

@Composable
fun LoginScreen(
    viewModel: LoginViewmodel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            value = uiState.username,
            onValueChange = viewModel::onUsernameChanged,
            label = {
                Text("Username")
            }
        )

        TextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChanged,
            label = {
                Text("Password")
            }
        )

        Button(
            onClick = viewModel::login,
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Login")
            }
        }

        uiState.error?.let {
            Text(it)
        }

        if (uiState.isLoggedIn) {
            Text("Login successful!")
        }
    }
}