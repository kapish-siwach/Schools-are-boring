package com.example.schoolsareboring.activity.attendance

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.activity.attendance.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.AttendanceMark
import com.example.schoolsareboring.models.StudentData

class ShowAttendance : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                ShowAttendanceScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAttendanceScreen() {
    val context= LocalActivity.current
    val viewModel: FirestoreViewModel = viewModel()
    val allAttendance = remember { mutableStateOf<Map<String, Map<String, AttendanceMark>>>(emptyMap()) }
    val allDates = remember { mutableStateListOf<String>() }
    val students by remember { derivedStateOf { viewModel.allStudents } }

    LaunchedEffect(Unit) {
        viewModel.listenToStudents()
        viewModel.loadAllAttendance { data ->
            allAttendance.value = data

            val dateSet = mutableSetOf<String>()
            data.values.forEach { map -> dateSet.addAll(map.keys) }
            allDates.clear()
            allDates.addAll(dateSet.sorted())

            // Debug output
            println("All Dates: $allDates")
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Attendance History") },
                navigationIcon = { IconButton(onClick = {(context as Activity).finish()}){
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, contentDescription = "back")
                } })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (students.isEmpty()) {
                Text("Loading students...", modifier = Modifier.padding(16.dp))
            } else {
                AttendanceTable(allAttendance.value, allDates, students)
            }
        }
    }
}

@Composable
fun AttendanceTable(
    attendanceData: Map<String, Map<String, AttendanceMark>>,
    allDates: List<String>,
    students: List<StudentData>
) {
    val scrollState = rememberScrollState()

    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {
            Row(Modifier.horizontalScroll(scrollState)) {
                Row(Modifier.padding(vertical = 4.dp)) {
                    Text("Name", Modifier.width(120.dp), fontWeight = FontWeight.Bold)
                    allDates.forEach { date ->
                        Text(
                            text = date.takeLast(5),
                            modifier = Modifier.width(60.dp),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }

        items(students) { student ->
            val attendance = attendanceData[student.regNo] ?: emptyMap()
            Row(Modifier.horizontalScroll(scrollState)) {
                Row(Modifier.padding(vertical = 4.dp)) {
                    Text(student.name, Modifier.width(120.dp))
                    allDates.forEach { date ->
                        val markChar = when (attendance[date]) {
                            AttendanceMark.PRESENT -> "P"
                            AttendanceMark.ABSENT -> "A"
                            AttendanceMark.LEAVE -> "L"
                            else -> "-"
                        }
                        val color = when (markChar) {
                            "P" -> Color(0xFF4CAF50)
                            "A" -> Color(0xFFF44336)
                            "L" -> Color(0xFFFFEB3B)
                            else -> Color.Gray
                        }
                        Text(
                            text = markChar,
                            modifier = Modifier.width(60.dp),
                            fontWeight = FontWeight.SemiBold,
                            color = color,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

