package com.example.schoolsareboring.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.ClassFilter
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.UserInputField
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AdmitCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {

                Greeting()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {
    val context = LocalContext.current
    val session = remember { PreferenceManager(context) }
    val isStudent = session.getData("userType") == "student"
    val viewModel: FirestoreViewModel = viewModel()
    val showBottomSheet = remember { mutableStateOf(false) }
    val studentRegNo = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val selectedClass = remember { mutableStateOf("All") }
    LaunchedEffect(Unit) {
        viewModel.listenToStudents()
    }
    val students = viewModel.allStudents

    val filteredStudents = students.filter {
        selectedClass.value == "All" || it.clazz == selectedClass.value
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Admit Card") }, navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "back",
                    Modifier.size(30.dp)
                )
            }
        })
    }, floatingActionButton = {

    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            if (showBottomSheet.value) {
                ModalBottomSheet(onDismissRequest = {
                    showBottomSheet.value = false
                }) {
                    AddAdmitCard(
                        scope, session, sheetState, showBottomSheet, viewModel, studentRegNo
                    )
                }
            }

            if (session.getData("userType") == "admin") {
                ClassFilter(
                    selectedClass = selectedClass.value,
                    onClassSelected = { selectedClass.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }

            val scrollState = rememberScrollState()
            LazyColumn {
                item {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                            .padding(horizontal = 5.dp)
                    ) {
                        Text(
                            "Name",
                            Modifier.width(85.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Reg no.",
                            Modifier.width(85.dp),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Class",
                            Modifier.width(85.dp),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Admit Card",
                            Modifier.width(85.dp),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                    HorizontalDivider(thickness = 1.dp)
                }
                items(filteredStudents) { student ->
                    OutlinedCard(
                        onClick = {
                            if (!isStudent) {
                                studentRegNo.value = student.regNo
                                showBottomSheet.value = true
                            }
                        },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .padding(2.dp)
                            .horizontalScroll(scrollState)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                student.name,
                                Modifier.width(85.dp),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                student.regNo,
                                Modifier.width(85.dp),
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                student.clazz,
                                Modifier.width(85.dp),
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                            TextButton(onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(student.admitCard))
                                try {
                                    context.startActivity(intent)
                                }catch (e:Exception){
                                    Toast.makeText(context,"Error opening file.", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Text(
                                    "Download",
                                    Modifier.width(80.dp),
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAdmitCard(
    scope: CoroutineScope,
    session: PreferenceManager,
    sheetState: SheetState,
    showBottomSheet: MutableState<Boolean>,
    viewModel: FirestoreViewModel,
    studentRegNo: MutableState<String>
) {
    val urlState = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        UserInputField(
            label = "URL",
            value = urlState,
            onValueChange = { urlState.value = it },
            singleLine = true,
            endIcon = Icons.Default.Add,
            enabled = true
        )
        ElevatedButton(onClick = {
            viewModel.updateAdmitCardUrl(studentRegNo.value, urlState.value)
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    showBottomSheet.value = false
                }
            }
        }, modifier = Modifier.fillMaxWidth(), enabled = urlState.value.trim()!="") {
            Text("Save", fontWeight = FontWeight.SemiBold)
        }
    }
}



