package com.example.schoolsareboring.activity.teachers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.R
import com.example.schoolsareboring.activity.student.AddStudentActivity
import com.example.schoolsareboring.activity.student.LabelWithValue
import com.example.schoolsareboring.activity.student.StudentCard
import com.example.schoolsareboring.activity.teachers.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.room.UserViewModel

class Teachers : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            SchoolsAreBoringTheme {
                TeachersScreen()
            }
        }
    }
}

@Composable
fun TeacherCard(teacher: TeachersData) {
    val context= LocalContext.current
    val imageUri = teacher.imageUri?.let { Uri.parse(it) }
    val preferenceManager =PreferenceManager(context)
    val viewModel:UserViewModel= viewModel()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { }
    ) {

        Row(modifier = Modifier
            .padding(5.dp).shadow(2.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Teacher Image",
                modifier = Modifier.weight(0.5f).padding(5.dp).shadow(2.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                LabelWithValue(label = "Name", value = teacher.name)
                LabelWithValue(label = "Email", value = teacher.email)
                LabelWithValue(label = "Subject", value = teacher.subject)
                LabelWithValue(label = "Father's Name", value = teacher.fatherName)
                LabelWithValue(label = "Phone", value = teacher.phone)
            }
            Column(Modifier.padding(5.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                IconButton(onClick = {val intent = Intent(context, AddTeachersActivity::class.java).apply {
                    putExtra("TeacherData", teacher)
                }
                    context.startActivity(intent)
                }, modifier = Modifier.padding(vertical = 10.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit",Modifier.size(30.dp))
                }
                if (preferenceManager.getData("userType")=="admin") {
                    IconButton(
                        onClick = { viewModel.deleteTeacher(teacher) },
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
@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachersScreen() {
    val context = LocalContext.current
    val viewModel: UserViewModel = viewModel()
    val teachers by viewModel.allTeachers.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teachers") },
                navigationIcon = {
                    IconButton (onClick = { (context as? Activity)?.finish()}){
                       Icon( Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                           contentDescription = "Back",
                           modifier = Modifier )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context, AddTeachersActivity::class.java))
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

            Spacer(modifier = Modifier.height(4.dp))
            if (teachers.isEmpty()) {
                Text("No data found!!", fontSize = 18 .sp)
            } else {
                LazyColumn {
                    items(teachers) { teacher ->
                        TeacherCard(teacher)
                    }
                }
            }
        }
    }
}

