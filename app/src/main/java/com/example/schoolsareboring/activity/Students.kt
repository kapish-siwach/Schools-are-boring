package com.example.schoolsareboring.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.schoolsareboring.R
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme

class Students : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SchoolsAreBoringTheme {
                StudentsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Students") },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .clickable {
                                (context as? Activity)?.finish()
                            }
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStudentsScreen() {
    SchoolsAreBoringTheme {
        StudentsScreen()
    }
}
