package com.thesua7.kmpplayground.viewmodel

import com.thesua7.kmpplayground.repository.LoginRepository
import io.ktor.util.rootCause
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewmodel(private val loginRepository: LoginRepository) {


    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    @OptIn(InternalAPI::class)
    fun login() {
        val currentState = _uiState.value
        scope.launch {
            _uiState.value = currentState.copy(isLoading = true, error = null)

            try {


                val response = loginRepository.login(currentState.username, currentState.password)


                _uiState.value = currentState.copy(isLoading = false, isLoggedIn =true)

                println("Token: ${response.accessToken}")


            } catch (e: Exception) {
                println("Error Text: ${e.cause}")
                println("Error Text: ${e.message}")
                println("Error Text: ${e.rootCause}")

                _uiState.value = currentState.copy(isLoading = false, error = e.message)


            }


        }


    }

    fun onUsernameChanged(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username
        )
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password
        )
    }

    fun onCleared(){
        scope.cancel()
    }

}