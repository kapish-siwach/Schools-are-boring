package com.example.schoolsareboring.activity.assignments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.schoolsareboring.activity.assignments.ui.theme.SchoolsAreBoringTheme

class AssignmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                    Greeting()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {

    val context= LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Assignments") },
                navigationIcon = { IconButton(onClick = {(context as Activity).finish()}){
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft,"back")
                } })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context,AddAssignment::class.java))
            }) {
                Icon(Icons.Default.Add,"add")
            }
        }
    ) {
        innerPadding->
        Column(modifier = Modifier.padding(innerPadding)) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview9() {
    SchoolsAreBoringTheme {
        Greeting()
    }
}