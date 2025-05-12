package com.example.schoolsareboring.activity.timetables

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.activity.syllabus.SyllabusCard
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel

class TimeTable : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                    TimeTableScreen(modifier = Modifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTableScreen(modifier: Modifier = Modifier) {
    val context= LocalContext.current
    val session = PreferenceManager(context)
    val viewModel:FirestoreViewModel= viewModel()
    LaunchedEffect(Unit) {
        viewModel.listenToTimetable()
    }
    val timetable=viewModel.allTimetable
    Column {
        Scaffold(topBar = {
            TopAppBar(title = { Text("Time Table") },
              navigationIcon = {
                  IconButton(onClick ={ (context as Activity).finish() }) {
                      Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "back",Modifier.size(30.dp))
                  }
              }  ) },
//            floatingActionButtonPosition = FabPosition.EndOverlay,
            floatingActionButton = {
                if(session.getData("userType")!="student"){
                FloatingActionButton(onClick = {context.startActivity(Intent(context,AddTimetable::class.java))}) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                }
                    }
            }
            ) {
            innerPadding->
            Column(modifier.padding(innerPadding).padding(10.dp)) {
                if (timetable.isEmpty()) {
                    Text("No data found")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(timetable) { timetable ->
                            SyllabusCard(timetable, session,viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    SchoolsAreBoringTheme {
        TimeTableScreen()
    }
}