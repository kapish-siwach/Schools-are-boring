package com.example.schoolsareboring.activity.results

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.AddDetailsBottomSheet
import com.example.schoolsareboring.OutlinedFileCard
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.activity.results.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                ResultsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen() {
    val context = LocalContext.current
    val session = remember { PreferenceManager(context) }
    val isStudent = session.getData("userType") == "student"
    val viewModel:FirestoreViewModel= viewModel()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.listenToResults()
    }
    val results=viewModel.allResults
    val loading=viewModel.isLoading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Results" +
                        "") },
                navigationIcon = {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, "back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (!isStudent) {
                ExtendedFloatingActionButton(
                    text = { Text("Add Result") },
                    icon = {Icon(Icons.Default.Add, "add")},
                    onClick = {showBottomSheet.value=true}
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 10.dp)) {
            if (showBottomSheet.value) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet.value = false
                    },
                    sheetState = sheetState,
                ) {
                    AddDetailsBottomSheet(scope,"resultActivity",sheetState,showBottomSheet)
                }
            }


            if (loading){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (results.isEmpty()){
                Text("No data found")
            }else{
             LazyVerticalGrid(
                 columns = GridCells.Fixed(2),
                 verticalArrangement = Arrangement.spacedBy(5.dp),
                 horizontalArrangement = Arrangement.spacedBy(12.dp),
                 modifier = Modifier.fillMaxHeight()
             ) {
                 items(results){result->
                     OutlinedFileCard(result,session,viewModel,"results")
                 }
             }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview7() {
    SchoolsAreBoringTheme {
        ResultsScreen()
    }
}