package com.example.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.login.theme.LoginSDKTheme
import com.example.loginsdk.R

class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mobNumber = intent.getStringExtra(EXTRA_MOBILE_NUMBER)
        setContent {
            val state by viewModel.loginState.collectAsStateWithLifecycle()
            LoginSDKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        number = mobNumber.orEmpty(),
                        modifier = Modifier.padding(innerPadding),
                        state = state,
                        context = this
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_MOBILE_NUMBER = "extra_mobile_number"
        const val RESULT_SUCCESS = "result_success"
    }

    @Composable
    fun LoginScreen(number: String, modifier: Modifier = Modifier, state: LoginViewModel.LoginState, context: Context) {
        var mobNumber by remember { mutableStateOf(number) }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(all=20.dp)
        ) {
            Text(
                text = stringResource(R.string.login_screen),
                modifier = modifier,
                fontSize =  MaterialTheme.typography.titleLarge.fontSize
            )
            OutlinedTextField(
                value = mobNumber,
                label = {
                    Text(
                        text =  stringResource(R.string.enter_the_mobile_number),
                        modifier = modifier
                    )
                },
                onValueChange = {
                    mobNumber = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            )
            Text(
                text =   if(state is LoginViewModel.LoginState.Error)  context.getString(R.string.please_enter_valid_data) else "",
                modifier = modifier.padding(top = 20.dp),
                color = Color.Red,
                fontSize =  MaterialTheme.typography.bodyLarge.fontSize
            )
            Button(
                onClick = {
                    viewModel.verifyLoginDetails(number, password)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            ) {
                if (state is LoginViewModel.LoginState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Yellow)
                } else {
                    Text(stringResource(R.string.login))
                }
            }
        }

        if (state is LoginViewModel.LoginState.Success) {
            Toast.makeText(this, this.getString(R.string.login_success), Toast.LENGTH_SHORT).show()
            val resultIntent = Intent().apply {
                putExtra(RESULT_SUCCESS, true)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginSDKTheme {
    }
}


