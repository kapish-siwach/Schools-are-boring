package com.example.schoolsareboring.activity.results

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.schoolsareboring.activity.results.ui.theme.SchoolsAreBoringTheme

class AddResult : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                    AddResultScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddResultScreen() {
    val context= LocalContext.current

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Assignments") },
            navigationIcon = {
                IconButton(onClick = { (context as Activity).finish() }) {
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, "back")
                }
            }
        )
    },) {
        innerPadding->
        Column(Modifier.padding(innerPadding)) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview10() {
    SchoolsAreBoringTheme {
        AddResultScreen()
    }
}