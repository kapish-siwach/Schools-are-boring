package com.example.schoolsareboring.activity.student

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.UserInputField
import com.example.schoolsareboring.activity.MainActivity
import com.example.schoolsareboring.activity.loginsignup.clearEntries
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel

class StudentLogin : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) { innerPadding ->
                    StudentLoginMethod(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun StudentLoginMethod(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val stuPhone = remember { mutableStateOf("") }
    val stuEmail = remember { mutableStateOf("") }
    val preferenceManager = remember { PreferenceManager(context) }
    val viewModel: FirestoreViewModel = viewModel()
    val errorMsg = remember { mutableStateOf("") }

    val emailError = remember { mutableStateOf("") }
    val phoneError = remember { mutableStateOf("") }
    Column(
        Modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            onClick = { (context as Activity).finish() },
            modifier = Modifier.padding(top = 10.dp, start = 13.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = "Back",
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Student",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                /*color = Color.Black,*/
                fontSize = 32.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Log in",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                /*color = Color.Black,*/
                fontSize = 26.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

                    UserInputField(
                        label = "Phone",
                        value = stuPhone,
                        endIcon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone,
                        enabled = true,
                    )


                    UserInputField(
                        label = "Email",
                        value = stuEmail,
                        endIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        enabled = true,
                    )


            if (errorMsg.value.trim().isNotEmpty()) {
                Text(
                    text = errorMsg.value,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        ElevatedButton(
            onClick = {
                if (stuEmail.value.isEmpty() || stuPhone.value.isEmpty()) {
                    errorMsg.value = "All fields are required!!"
                } else {
                    viewModel.checkStudentCredentials(email = stuEmail.value, phone = stuPhone.value) { student ->
                        if (student != null) {
                            errorMsg.value = ""
                            preferenceManager.setLoggedIn(true)
                            preferenceManager.saveData("userType","student")
                            preferenceManager.saveData("name",student.name)
                            preferenceManager.saveUserData("userData",student)
                            val intent=Intent(context,MainActivity::class.java)
                            context.startActivity(intent)
                            clearEntries(stuEmail, stuPhone)
                        } else {
                            errorMsg.value = "Student not found !!"
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 20.dp, start = 20.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(
                "Log in",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SchoolsAreBoringTheme {
        StudentLoginMethod()
    }
}