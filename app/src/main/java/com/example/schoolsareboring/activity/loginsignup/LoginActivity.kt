package com.example.schoolsareboring.activity.loginsignup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.schoolsareboring.activity.MainActivity
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.firestore.handleGoogleSignInResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

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
fun LoginScreen(
    modifier: Modifier = Modifier,
    firestoreViewModel: FirestoreViewModel = viewModel()
) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val errorMsg = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val preferenceManager = remember { PreferenceManager(context) }
    val activity = context as Activity
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleGoogleSignInResult(
            result,
            context,
            firestoreViewModel,
            preferenceManager,
            onSuccess = {
                Toast.makeText(context, "Signed in successfully!", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            onFailure = {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        )
    }


    Column(
        modifier = Modifier
            .padding(top = 20.dp, start = 5.dp, end = 5.dp)
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            onClick = { (context as Activity).finish() },
            modifier = Modifier
                .padding(top = 6.dp, start = 3.dp)
                .clip(CircleShape)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
            )
        }

        Column(
            modifier = Modifier.padding(top = 15.dp, start = 8.dp)
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
                        Icon(
                            painter = painterResource(id = image),
                            contentDescription = description
                        )
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
                        errorMsg.value = "All fields are required!"
                    } else {
                        firestoreViewModel.getAdminByEmailAndPassword(
                            email = email.value,
                            password = password.value
                        ) { user ->
                            if (user != null) {
                                errorMsg.value = ""
                                preferenceManager.setLoggedIn(true)
                                preferenceManager.saveData("name", user.name ?: "")
                                preferenceManager.saveData("email", user.email ?: "")
                                preferenceManager.saveData("userType", user.role ?: "admin")
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
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                },
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
                    Text(
                        "Sign up with Google",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }

            TextButton(onClick = {
                context.startActivity(Intent(context, UsertypeActivity::class.java))
            }) {
                Row {
                    Text("Want to change user type?", color = Color.Blue)
                }
            }
        }
    }
}

    fun clearEntries(email: MutableState<String>, password: MutableState<String>) {
        email.value = ""
        password.value = ""
    }

