package com.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun verifyLoginDetails(mobNumber: String, password: String){
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            delay(1000) // simulate network
            if (mobNumber.isNotEmpty() && password.isNotEmpty()) {
                _loginState.value = LoginState.Success(mobNumber)
            } else {
                _loginState.value = LoginState.Error("Invalid Data")
            }
        }
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val mobNumber: String) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}