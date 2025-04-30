package com.example.schoolsareboring.activity.syllabus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.R
import com.example.schoolsareboring.activity.syllabus.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.models.SyllabusModal
import com.example.schoolsareboring.room.UserViewModel

class Syllabus : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                SyllabusScreen(modifier = Modifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyllabusScreen(modifier: Modifier = Modifier) {
    val context= LocalContext.current
    val session=PreferenceManager(context)
    val viewModel:UserViewModel= viewModel()
    val syllabusModal by viewModel.getAllSyllabus.collectAsState(initial = emptyList())

    Column {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    title = { Text("Syllabus") },
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
               if (session.getData("userType")=="admin") {
                   FloatingActionButton(onClick = {
                       context.startActivity(Intent(context, AddSyllabus::class.java).apply {
                           putExtra("userType", session.getData("userType"))
                       })
                   }) {
                       Icon(Icons.Default.Add, contentDescription = "addSyllabus")
                   }
               }
           }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

                LazyVerticalGrid(columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxHeight()) {

                    items(syllabusModal){syllabus->
                        SyllabusCard(syllabus)
                    }
                }
                }
        }
    }
}

@Composable
fun SyllabusCard(syllabus: SyllabusModal) {

    val context= LocalContext.current

    Card (modifier = Modifier.padding(10.dp).fillMaxSize()){
        Column(modifier = Modifier.padding(5.dp).fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = syllabus.clazz)
            IconButton(onClick = {Toast.makeText(context,"Coming soon..",Toast.LENGTH_SHORT).show()}) {
                Row {
                    Image(painter = painterResource(id = R.drawable.download), contentDescription = "download")
                    Text("Download File")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SchoolsAreBoringTheme {
        SyllabusScreen()
    }
}