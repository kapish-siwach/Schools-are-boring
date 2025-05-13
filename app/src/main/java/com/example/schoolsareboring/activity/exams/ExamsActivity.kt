package com.example.schoolsareboring.activity.exams

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.ClassDropdownPicker
import com.example.schoolsareboring.DetailedCardView
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.UserInputField
import com.example.schoolsareboring.activity.assignments.AddAssignment
import com.example.schoolsareboring.activity.exams.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.currentDate
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.ExamsModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ExamsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                ExamsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamsScreen() {

    val context = LocalContext.current
    val session = remember { PreferenceManager(context) }
    val isStudent = session.getData("userType") == "student"
    val viewModel: FirestoreViewModel = viewModel()
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val selectedExam = remember { mutableStateOf<ExamsModal?>(null) }

    LaunchedEffect(Unit) {
        viewModel.listenToExams()
    }
    val examData = viewModel.allExams
    val loading = viewModel.isLoading
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Exams") },
                navigationIcon = {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "back",
                            Modifier.size(30.dp)
                        )
                    }
                })
        },
        floatingActionButton = {
            if (!isStudent) {
                FloatingActionButton(onClick = {
                    showBottomSheet.value = true
                }) {
                    Icon(Icons.Default.Add, "add")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
        ) {
            if (showBottomSheet.value) {
                ModalBottomSheet(onDismissRequest = {
                    showBottomSheet.value = false
                    selectedExam.value = null
                }) {
                    ExamEditSheet(viewModel, session, scope, sheetState, showBottomSheet,selectedExam.value)
                }
            }
            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (examData.isEmpty()) {
                Text("No data found")
            } else {
                LazyColumn {
                    items(examData) { exam ->
                        DetailedCardView(context,session,isStudent,exam,
                            onDelete = {viewModel.deleteExams(exam.clazz)},
                            onEdit = {selectedExam.value = exam
                                showBottomSheet.value = true})
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamEditSheet(
    viewModel: FirestoreViewModel,
    session: PreferenceManager,
    scope: CoroutineScope,
    sheetState: SheetState,
    showBottomSheet: MutableState<Boolean>,
    existingExam: ExamsModal?
) {
    val clazz = remember { mutableStateOf(existingExam?.clazz ?: "") }
    val note = remember { mutableStateOf(existingExam?.note ?:"") }
    val fileUrl = remember { mutableStateOf(existingExam?.fileUrl ?:"") }
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ClassDropdownPicker(
            selectedClass = clazz.value,
            onClassSelected = { clazz.value = it },
            enabled = existingExam==null
        )

        UserInputField(
            label = "Paste drive file link",
            value = fileUrl,
            endIcon = Icons.Default.Add,
            onValueChange = { fileUrl.value = it },
            singleLine = true
        )

        UserInputField(
            label = "Add note",
            value = note,
            enabled = true,
            endIcon = Icons.Default.Edit,
            onValueChange = { note.value = it },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(10.dp))
        ElevatedButton(
            onClick = {
                val data = ExamsModal(
                    clazz = clazz.value,
                    note = note.value,
                    fileUrl = fileUrl.value,
                    date = currentDate
                )

                viewModel.addExams(data)

                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                }

            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            enabled = clazz.value != "" && fileUrl.value.trim() != ""
        ) {
            Text(
                "Submit",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}
