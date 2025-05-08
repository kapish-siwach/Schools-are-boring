package com.example.schoolsareboring.activity.syllabus

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.R
import com.example.schoolsareboring.activity.syllabus.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.SyllabusModal

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
    val context = LocalContext.current
    val session = PreferenceManager(context)
    val viewModel: FirestoreViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.listenToSyllabus()
    }
    val syllabusModal = viewModel.allSyllabus
    Column {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    title = { Text("Syllabus") },
                    navigationIcon = {
                        IconButton(onClick = {  (context as? Activity)?.finish()}) {
                            Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, contentDescription = "back")
                        }
                    }
                )
            },
            floatingActionButton = {
                if (session.getData("userType") == "admin") {
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
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (syllabusModal.isEmpty()) {
                    Text("No data found")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {

                        items(syllabusModal) { syllabus ->
                            SyllabusCard(syllabus, session,viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SyllabusCard(syllabus: SyllabusModal, session: PreferenceManager,viewModel:FirestoreViewModel) {

    val context = LocalContext.current
    Column {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(syllabus.fileUrl))
                        context.startActivity(intent)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Class ${syllabus.clazz}")

                Image(
                    painter = painterResource(id = R.drawable.book),
                    contentDescription = "download",
                    Modifier
                        .height(70.dp)
                        .padding(5.dp)
                )
            }
        }
        if (session.getData("userType")=="admin") {
            IconButton(
                onClick = { viewModel.deleteSyllabus(syllabus.clazz) },
                modifier = Modifier.offset( y = (-30).dp).size(35.dp)
                    .align(Alignment.End),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White
                )
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.size(35.dp).padding(2.dp),
                    colorResource(R.color.red)
                )
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