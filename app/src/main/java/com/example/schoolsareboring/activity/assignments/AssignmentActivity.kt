package com.example.schoolsareboring.activity.assignments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.ClassFilter
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.activity.assignments.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.SyllabusModal

class AssignmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                AssignmentScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentScreen() {
    val context = LocalContext.current
    val viewModel: FirestoreViewModel = viewModel()
    val session = remember { PreferenceManager(context) }
    val selectedClass = remember { mutableStateOf("All") }

    val loading = viewModel.isLoading
    val assignments = viewModel.allAssignment

    val isStudent = session.getData("userType") == "student"
    val student: StudentData? = if (isStudent) session.getUserData("userData") else null

    LaunchedEffect(student) {
        if (student != null) {
            selectedClass.value = student.clazz
        }
    }

    LaunchedEffect(Unit) {
        viewModel.listenToAssignment()
    }

    val filteredAssignment = assignments.filter {
        selectedClass.value == "All" || it.clazz == selectedClass.value
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Assignments") }, navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, "back")
            }
        })
    }, floatingActionButton = {
        if (!isStudent) {
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context, AddAssignment::class.java))
            }) {
                Icon(Icons.Default.Add, "add")
            }
        }
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (!isStudent) {
                ClassFilter(
                    selectedClass = selectedClass.value,
                    onClassSelected = { selectedClass.value = it },
                )
            }

            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (filteredAssignment.isEmpty()) {
                Text("No data found..", modifier = Modifier.padding(10.dp))
            } else {
                LazyColumn {
                    items(filteredAssignment) { assignment ->
                        ShowAssignments(assignment, viewModel, context, isStudent)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowAssignments(
    assignemt: SyllabusModal, viewModel: FirestoreViewModel, context: Context, isStudent: Boolean
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Class: ${assignemt.clazz} \t (${assignemt.date})",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)
            )
            if (!isStudent) {
                Row {
                    IconButton(onClick = {
                        viewModel.deleteAssignment(assignemt.clazz)
                    }, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)) {
                        Icon(Icons.Default.Delete, contentDescription = "delete")
                    }

                    IconButton(onClick = {
                        context.startActivity(Intent(context, AddAssignment::class.java).apply {
                            putExtra("allData", assignemt)
                            putExtra("isEditing", true)
                        })
                    }) {
                        Icon(Icons.Default.Edit, "edit")
                    }
                }
            }

        }
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Color.DarkGray)
        )

        Text(
            assignemt.fileUrl, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview9() {
    SchoolsAreBoringTheme {
        AssignmentScreen()
    }
}
