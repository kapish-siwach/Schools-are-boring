package com.example.schoolsareboring.activity.syllabus

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.AddDetailsBottomSheet
import com.example.schoolsareboring.OutlinedFileCard
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.activity.syllabus.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel

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

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }
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
                    ExtendedFloatingActionButton(
                        text = { Text("Add Syllabus") },
                        icon = {Icon(Icons.Default.Add, "add")},
                        onClick = {showBottomSheet.value=true}
                    )
                }
            }

        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {

                if (showBottomSheet.value){
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet.value=false
                        },
                        sheetState = sheetState
                    ) {
                        AddDetailsBottomSheet(scope,"syllabusActivity", sheetState, showBottomSheet)
                    }
                }

                if (syllabusModal.isEmpty()) {
                    Text("No data found")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {

                        items(syllabusModal) { syllabus ->
                            OutlinedFileCard(syllabus, session,viewModel,"syllabus")
                        }
                    }
                }
            }
        }
    }
}
