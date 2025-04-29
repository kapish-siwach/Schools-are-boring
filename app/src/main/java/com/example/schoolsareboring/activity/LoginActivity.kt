package com.example.schoolsareboring.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.R
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.room.UserViewModel
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val errorMsg = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val preferenceManager = remember { PreferenceManager(context) }
    val viewModel: UserViewModel = viewModel()
    Column(
        modifier = Modifier
            .padding(top = 20.dp, start = 5.dp, end = 5.dp)
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .padding(top = 15.dp, start = 8.dp)
        ) {
            Text(
                text = "Log in",
                modifier = modifier.padding(start = 20.dp),
                textAlign = TextAlign.Start,
                color = Color.Black,
                fontSize = 26.sp,
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it.trim() },
                label = { Text(text = "Email") },
                trailingIcon = { Icon(Icons.Default.Email, contentDescription = "email") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it.trim() },
                label = { Text(text = "Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        R.drawable.baseline_visibility_24
                    else R.drawable.baseline_visibility_off_24
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(id = image), contentDescription = description)
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            if (errorMsg.value.trim().isNotEmpty()) {
                Text(
                    text = errorMsg.value,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }


            ElevatedButton(
                onClick = {
                    if (email.value.isEmpty() || password.value.isEmpty()) {
                        errorMsg.value = "All fields are required!!"
                    } else {
                        viewModel.checkUserCredentials(email.value, password.value) { admin ->
                            if (admin!=null) {
                                errorMsg.value = ""
                                preferenceManager.setLoggedIn(true)
                                preferenceManager.saveData("name",admin.name)
                                preferenceManager.saveData("userType","admin")
                                preferenceManager.saveUserData("userData",admin)
                                context.startActivity(Intent(context, MainActivity::class.java))
                                clearEntries(email, password)
                            } else {
                                errorMsg.value = "Invalid email or password!"
                            }
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, end = 10.dp, start = 10.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text("Log in", textAlign = TextAlign.Center, fontSize = 16.sp)
            }

            TextButton(onClick = {
                context.startActivity(Intent(context, SignupActivity::class.java))
            }) {
                Row {
                    Text("Don't have an account? ")
                    Text("Sign up", color = Color.Blue)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Or", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = { Toast.makeText(context, "Coming soon!!", Toast.LENGTH_SHORT).show() },
                border = BorderStroke(2.dp, Color.DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google logo",
                        modifier = Modifier
                            .padding(5.dp)
                            .size(24.dp)
                    )
                    Text("Sign up with Google", textAlign = TextAlign.Center, modifier = Modifier.padding(5.dp))
                }
            }

            TextButton(onClick = {
                context.startActivity(Intent(context, UsertypeActivity::class.java))
            }) {
                Row {
                    Text("Want to change user type?" ,color = Color.Blue)
                }
            }
        }
    }
}


fun checkUserInput(
    email: MutableState<String>,
    password: MutableState<String>,
    preferenceManager: PreferenceManager,
    errorMsg: MutableState<String>
): Boolean {
    val savedEmail = preferenceManager.getData("email")
    val savedPassword = preferenceManager.getData("password")

    return if (email.value.trim() != savedEmail) {
        errorMsg.value = "Email not found!!"
        false
    } else if (password.value.trim() != savedPassword) {
        errorMsg.value = "Incorrect password!!"
        false
    } else {
        true
    }
}

fun clearEntries(email: MutableState<String>, password: MutableState<String>) {
    email.value = ""
    password.value = ""
}
