package com.example.schoolsareboring.activity.TimeTable

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.ui.unit.dp
import com.example.schoolsareboring.activity.TimeTable.ui.theme.SchoolsAreBoringTheme

class TimeTable : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                    TimeTable(modifier = Modifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTable(modifier: Modifier = Modifier) {
    val context= LocalContext.current
    Column {
        Scaffold(topBar = {
            TopAppBar(title = { Text("Time Table") },
              navigationIcon = {
                  IconButton(onClick ={ (context as Activity).finish() }) {
                      Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "back",Modifier.size(30.dp))
                  }
              }  )
        }) {
            innerPadding->
            Column(modifier.padding(innerPadding)) {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    SchoolsAreBoringTheme {
        TimeTable()
    }
}