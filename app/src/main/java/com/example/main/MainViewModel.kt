package com.example.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _mobileVerifyState = MutableStateFlow<MobileVerifyState>(MobileVerifyState.Idle)
    val mobileVerifyState: StateFlow<MobileVerifyState> = _mobileVerifyState

    fun isValidMobile(mobile: String) {
        when{
            mobile.length == MAX_MOBILE_LENGTH ->{
                _mobileVerifyState.value = MobileVerifyState.Valid(mobile)
            }
            else -> {
                _mobileVerifyState.value = MobileVerifyState.Error("Invalid Data")
            }
        }
    }

    sealed class MobileVerifyState {
        object Idle : MobileVerifyState()
        data class Valid(val mobNumber: String) : MobileVerifyState()
        data class Error(val message: String) : MobileVerifyState()
    }

}