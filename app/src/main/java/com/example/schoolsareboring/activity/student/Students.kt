package com.example.schoolsareboring.activity.student

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.schoolsareboring.ClassFilter
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.R
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
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
    val viewModel: FirestoreViewModel = viewModel()
    val searchQuery = remember { mutableStateOf("") }
    val selectedClass = remember { mutableStateOf("All") }

    var loading by remember { mutableStateOf(false) }
    loading=true
    LaunchedEffect(Unit) {
        loading=true
        viewModel.listenToStudents()
    }
    val students=viewModel.allStudents

    val filteredStudents = students.filter {
        it.name.contains(searchQuery.value, ignoreCase = true) &&
                (selectedClass.value == "All" || it.clazz == selectedClass.value)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Students") },
                navigationIcon = {
                   IconButton(onClick = {(context as? Activity)?.finish()}) {
                        Icon(
                            Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context, AddStudentActivity::class.java))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add New")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(4.dp)
        ) {

            Row(modifier= Modifier
                .fillMaxWidth()
                .padding(10.dp, 1.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {

                ClassFilter(selectedClass = selectedClass.value,
                    onClassSelected = { selectedClass.value=it },
                    modifier = Modifier.weight(0.1f)
                    )


                // Search bar
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    label = { Text("Search") },
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(0.1f),
                    leadingIcon = {Icon(Icons.Default.Search, contentDescription = "Search")},
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            if (loading){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (filteredStudents.isEmpty()) {
                loading=false
                Text("No students found.", modifier = Modifier.padding(16.dp))
            } else {
                loading=false
                LazyColumn {
                    items(filteredStudents) { student ->
                        StudentCard(student)
                    }
                }
            }
        }
    }
}


@Composable
fun StudentCard(student: StudentData) {
    val context= LocalContext.current
    val imageUri = student.imageUri?.let { Uri.parse(it) }
    val userViewModel:FirestoreViewModel= viewModel()
    val preferenceManager=PreferenceManager(context)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        onClick = {}
    ) {

        Row(modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Student Image",
                modifier = Modifier
                    .weight(0.5f)
                    .padding(5.dp)
                    .height(150.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Column(modifier = Modifier
                .padding(6.dp)
                .weight(1f)) {
                LabelWithValue(label = "Name", value = student.name)
                LabelWithValue(label = "Reg. No", value = student.regNo)
                LabelWithValue(label = "Class", value = student.clazz)
                LabelWithValue(label = "Roll No", value = student.rollNo)
                LabelWithValue(label = "Father's Name", value = student.fatherName)
                LabelWithValue(label = "Phone", value = student.phone)
            }

            Column(
                Modifier
                    .padding(5.dp)
                    .fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                IconButton(onClick = {val intent = Intent(context, AddStudentActivity::class.java).apply {
                    putExtra("studentData", student)
                    putExtra("firstT",true)
                }
                    context.startActivity(intent)}, modifier = Modifier.padding(vertical = 10.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit",Modifier.size(30.dp))
                }
                if (preferenceManager.getData("userType")=="admin") {
                    IconButton(
                        onClick = { userViewModel.deleteStudent(student.regNo) },
                        modifier = Modifier.padding(vertical = 10.dp),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            Modifier.size(30.dp)
                        )
                    }
                }
            }
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
