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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.schoolsareboring.models.UserData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues())) { innerPadding ->
                    Signup(
                        modifier = Modifier.padding(innerPadding)/*.statusBarsPadding()*/
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Signup(modifier: Modifier = Modifier,firestoreVIewModal: FirestoreViewModel= viewModel()) {
    val context = LocalContext.current
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confmPassword = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val errorMsg = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var conPasswordVisible by remember { mutableStateOf(false) }
    val preferenceManager = remember { PreferenceManager(context) }


    val activity = context as Activity

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

// Google Sign-In Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleGoogleSignInResult(
            result,
            context,
            firestoreVIewModal,
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
            .padding(top = 20.dp, start = 5.dp,end=5.dp)
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = { context.finish() },
            modifier = Modifier.padding(top=6.dp,start=3.dp).clip(CircleShape)) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                )
        }
        Column(
            modifier = Modifier
                .padding(top=10.dp,start=8.dp/*,end=8.dp*/, /*bottom = 8.dp*/)
               ,
        ) {

            Text(
                text = "Sign Up",
                modifier = modifier.padding(start=20.dp),
                textAlign = TextAlign.Center,
               /* color = Color.Black,*/
                fontSize = 26.sp,
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name.value,
                trailingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                onValueChange = { name.value = it.trim() },
                label = { Text(text = "Name") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )

            OutlinedTextField(
                value = email.value,
                trailingIcon = { Icon(Icons.Default.Email, contentDescription = "email") },
                onValueChange = { email.value = it.trim() },
//                placeholder = { Text(text = "Email") },
                label = { Text(text = "Email") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it.trim()
                    passwordError.value = if (confmPassword.value.isNotEmpty() && it != confmPassword.value) {
                        "Passwords don't match!"
                    } else {
                        ""
                    }
                },
//              placeholder = { Text(text = "Password") },
                label = { Text(text = "Password") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
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
                isError = passwordError.value.isNotEmpty()
            )

            OutlinedTextField(
                value = confmPassword.value,
                onValueChange = {
                    confmPassword.value = it.trim()
                    passwordError.value = if (password.value != it) {
                        "Passwords don't match!"
                    } else {
                        ""
                    }
                },
//                placeholder = { Text(text = "Confirm Password") },
                label = { Text(text = "Confirm Password") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                visualTransformation = if (conPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (conPasswordVisible)
                        R.drawable.baseline_visibility_24
                    else R.drawable.baseline_visibility_off_24
                    val description = if (conPasswordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { conPasswordVisible = !conPasswordVisible }) {
                        Icon(painter = painterResource(id = image), contentDescription = description)
                    }
                },
                isError = passwordError.value.isNotEmpty()
            )
            if (passwordError.value.trim().isNotEmpty()) {
                Text(
                    text = passwordError.value,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (errorMsg.value.trim().isNotEmpty()) {
                Text(
                    text = errorMsg.value,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(22.dp))

            ElevatedButton(
                onClick = {
                    if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank() || confmPassword.value.isBlank()) {
                        errorMsg.value = "All fields are required!!"
                    } else if (passwordError.value.trim().isNotEmpty()) {
                        errorMsg.value = passwordError.value
                    } else {
                        firestoreVIewModal.checkAdminEmailExists(email.value) { emailExists ->
                            if (emailExists) {
                                errorMsg.value = "Email already exists!"
                            } else {
                                val user = UserData(
                                    id = email.value,
                                    name = name.value,
                                    email = email.value,
                                    password = password.value,
                                    role = "admin"
                                )
                                firestoreVIewModal.addAdmin(user)
                                preferenceManager.setLoggedIn(true)
                                preferenceManager.saveData("name", name.value)
                                preferenceManager.saveData("email", email.value)
                                preferenceManager.saveData("userType", "admin")
                                context.startActivity(Intent(context, MainActivity::class.java))
                                clearEntries(name, email, password, confmPassword)
                            }
                        }
                    }
                }
                , modifier = Modifier.fillMaxWidth().padding(top=10.dp, end = 10.dp,start=10.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )

            ) {
                Text("Sign up", textAlign = TextAlign.Center, fontSize = 16.sp)
            }

            TextButton(onClick = {
                context.startActivity(Intent(context, LoginActivity::class.java))
            }) {
                Row { Text("Already have an account? ")
                    Text("Log in", color = Color.Blue)}
            }

            Spacer(modifier=Modifier.height(8.dp))

            Text("Or", color = Color.Gray, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(10.dp))

            val googlePresses= remember { mutableStateOf(false) }
            TextButton(onClick = {
                googlePresses.value=true
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)},
                border = BorderStroke(2.dp,Color.DarkGray),
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp)
            ) {
                Row(modifier=Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google logo",
                        modifier = Modifier
                            .padding(5.dp)
                            .size(24.dp)
                    )
                    if (googlePresses.value){
                        CircularProgressIndicator(modifier.size(24.dp).padding(5.dp))
                    } else{
                Text("Sign up with Google", textAlign = TextAlign.Center, modifier = Modifier.padding(5.dp))
                        }
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

fun clearEntries(
    name: MutableState<String>,
    email: MutableState<String>,
    password: MutableState<String>,
    confmPassword: MutableState<String>
) {
    name.value=""
    email.value=""
    password.value=""
    confmPassword.value=""
}