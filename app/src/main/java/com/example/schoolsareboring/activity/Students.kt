package com.example.schoolsareboring.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.R
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.room.UserViewModel

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
    val viewModel: UserViewModel = viewModel()
    val students by viewModel.allStudents.collectAsState(initial = emptyList())
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
        floatingActionButton = {
            FloatingActionButton(onClick = {context.startActivity(Intent(context,AddStudentActivity::class.java))}) {
                Icon(Icons.Default.Add, contentDescription = "add new")
            }
        },
        contentWindowInsets = WindowInsets.systemBars,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top=5.dp, start = 5.dp, end = 5.dp)
        ) {
            if (students.isEmpty()) {
                Text("No students found.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(students) { student ->
                        StudentCard(student)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentCard(student: StudentData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            LabelWithValue(label = "Name", value = student.name)
            LabelWithValue(label = "Reg. No", value = student.regNo.toString())
            LabelWithValue(label = "Class", value = student.clazz)
            LabelWithValue(label = "Roll No", value = student.rollNo)
            LabelWithValue(label = "Father's Name", value = student.fatherName)
            LabelWithValue(label = "Mother's Name", value = student.motherName)
            LabelWithValue(label = "Phone", value = student.phone)
            LabelWithValue(label = "Email", value = student.email)
            LabelWithValue(label = "Gender", value = student.gender)
            LabelWithValue(label = "Date of Birth", value = student.dob)
        }

    }
}

@Composable
fun LabelWithValue(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "$label: ",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStudentsScreen() {
    SchoolsAreBoringTheme {
        StudentsScreen()
    }
}
