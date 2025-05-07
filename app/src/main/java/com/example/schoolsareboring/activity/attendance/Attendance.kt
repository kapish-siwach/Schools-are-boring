package com.example.schoolsareboring.activity.attendance

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.schoolsareboring.ClassFilter
import com.example.schoolsareboring.DateSelector
import com.example.schoolsareboring.activity.attendance.ui.theme.LightGreen
import com.example.schoolsareboring.activity.attendance.ui.theme.LightRed
import com.example.schoolsareboring.activity.attendance.ui.theme.LightYellow
import com.example.schoolsareboring.activity.attendance.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.activity.student.LabelWithValue
import com.example.schoolsareboring.currentDate
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.AttendanceMark
import com.example.schoolsareboring.models.StudentData

class Attendance : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                AttendanceScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen() {
    val context = LocalContext.current
    val viewModel: FirestoreViewModel = viewModel()
    val sDate = remember { mutableStateOf(currentDate) }
    val classFilter = remember { mutableStateOf("All") }

    LaunchedEffect(sDate.value) {
        viewModel.listenToStudents()
    }

    LaunchedEffect(sDate.value) {
        viewModel.loadAttendanceForDate(sDate.value)
    }

    val students = viewModel.allStudents

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Attendance")
                Text(sDate.value, fontSize = 16.sp, modifier = Modifier.padding(end = 5.dp))
                } },
                navigationIcon = {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(
                            Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {


          /*  DateSelector(
                sDate = sDate.value,
                onDateSelected = { sDate.value = it },
                enabled = true
            )*/

            ClassFilter(
                classFilter.value,
                onClassSelected = { classFilter.value = it },
                modifier = Modifier.fillMaxWidth()
            )

            val filteredStudents = students.filter {
                classFilter.value == "All" || it.clazz == classFilter.value
            }

            if (filteredStudents.isEmpty()) {
                Text("No Students Found")
            } else {
                LazyColumn {
                    items(filteredStudents) { student ->
                        StudentAttendanceCard(student, context, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentAttendanceCard(student: StudentData, context: Context, viewModel: FirestoreViewModel) {
    val imageUri = student.imageUri?.let { Uri.parse(it) }
    val selectedMark = viewModel.attendanceSelections[student.regNo]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        onClick = {}
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Student Image",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(5.dp)
                        .height(100.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )

                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .weight(1f)
                ) {
                    LabelWithValue(label = "Name", value = student.name)
                    LabelWithValue(label = "Reg. No", value = student.regNo)
                    LabelWithValue(label = "Class", value = student.clazz)
                    LabelWithValue(label = "Roll No", value = student.rollNo)
                }
            }

            Row(
                Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    onClick = {
                        viewModel.markStudentAttendance(student.regNo, AttendanceMark.PRESENT)
                    },
                    border = BorderStroke(1.dp, Color.DarkGray),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if (selectedMark == AttendanceMark.PRESENT) LightGreen else Color.Transparent
                    )
                ) {
                    Text("Present", fontWeight = FontWeight.SemiBold, color = Color.Black)
                }

                TextButton(
                    onClick = {
                        viewModel.markStudentAttendance(student.regNo, AttendanceMark.LEAVE)
                    },
                    border = BorderStroke(1.dp, Color.DarkGray),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if (selectedMark == AttendanceMark.LEAVE) LightYellow else Color.Transparent
                    )
                ) {
                    Text("On Leave", fontWeight = FontWeight.SemiBold, color = Color.Black)
                }

                TextButton(
                    onClick = {
                        viewModel.markStudentAttendance(student.regNo, AttendanceMark.ABSENT)
                    },
                    border = BorderStroke(1.dp, Color.DarkGray),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if (selectedMark == AttendanceMark.ABSENT) LightRed else Color.Transparent
                    )
                ) {
                    Text("Absent", fontWeight = FontWeight.SemiBold, color = Color.Black)
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.NONE
)
@Composable
fun GreetingPreview6() {
    SchoolsAreBoringTheme {
        AttendanceScreen()
    }
}
