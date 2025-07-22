package com.example.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.login.LoginActivity
import com.example.main.ui.theme.LoginSDKTheme
import com.example.loginsdk.R

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.mobileVerifyState.collectAsStateWithLifecycle()
            LoginSDKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        context= this,
                        state = state,
                        viewModel=viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier,
               context: Context,
               viewModel: MainViewModel,
               state : MainViewModel.MobileVerifyState
) {
    var mobNumber by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            success = data?.getBooleanExtra(LoginActivity.RESULT_SUCCESS, false)?:false
        }
    }
    Column(
        modifier = Modifier.padding(all=20.dp)
    ) {


        Text(
            text = stringResource(R.string.home_screen),
            modifier = modifier,
            fontSize =  MaterialTheme.typography.titleLarge.fontSize
        )
        OutlinedTextField(
            value = mobNumber,
            label = {
                Text(
                    text = stringResource(R.string.enter_the_mobile_number),
                    modifier = modifier
                )
            },
            onValueChange = {
                if(it.length <= MAX_MOBILE_LENGTH ){
                    viewModel.isValidMobile(it)
                    mobNumber = it
                }

            },
            isError = (state is MainViewModel.MobileVerifyState.Error),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            enabled = !success,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
        Button(
            onClick = {
                if (mobNumber.isBlank()) {
                    errorText = context.getString(R.string.mobile_and_password_must_not_be_empty)
                } else {
                    if(!success){
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.apply {
                            putExtra(LoginActivity.EXTRA_MOBILE_NUMBER, mobNumber)
                        }
                        launcher.launch(intent)
                    }else{
                        success = false
                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            if(success){
                Text(stringResource(R.string.login_success))
            }else
                Text(stringResource(R.string.submit))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginSDKTheme {
    }
}