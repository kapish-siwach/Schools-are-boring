package com.example.schoolsareboring.activity.assignments

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
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
import com.example.schoolsareboring.activity.assignments.ui.theme.SchoolsAreBoringTheme

class AddAssignment : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                AddAssignmentScreen()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentScreen(){
    val context= LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Assignments") },
                navigationIcon = { IconButton(onClick = {(context as Activity).finish()}){
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft,"back")
                } })
        },
    ) {
        innerPadding->
        Column(Modifier.padding(innerPadding)) {

        }
    }
}