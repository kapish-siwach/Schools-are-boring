package com.example.schoolsareboring.activity.assignments

import android.app.Activity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Update
import com.example.schoolsareboring.ClassDropdownPicker
import com.example.schoolsareboring.UserInputField
import com.example.schoolsareboring.activity.assignments.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.SyllabusModal

class AddAssignment : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

            enterTransition = Slide()

            exitTransition = Fade()
        }
        val assignmentData=intent.getSerializableExtra("allData") as? SyllabusModal
        val editable=intent.getBooleanExtra("isEditing",false)
        setContent {
            SchoolsAreBoringTheme {
                AddAssignmentScreen(assignmentData,editable)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentScreen(assignmentData: SyllabusModal?, editable: Boolean) {
    val context = LocalContext.current
    val clazz = remember { mutableStateOf(assignmentData?.clazz ?: "") }
    val description = remember { mutableStateOf(assignmentData?.fileUrl ?: "") }
    val viewModel : FirestoreViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Assignments") },
                navigationIcon = {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, "back")
                    }
                })
        },
    ) {

            innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            UserInputField(
                value = description,
                onValueChange = { description.value = it },
                label = "Describe",
                endIcon = Icons.Default.Create,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            ClassDropdownPicker(
                selectedClass = clazz.value,
                onClassSelected = { clazz.value = it },
                enabled = true
            )

            ElevatedButton(onClick = {
                val assignment=SyllabusModal(
                    clazz = clazz.value,
                    fileUrl = description.value
                )
                viewModel.addAssignment(assignment)
                (context as Activity).finish()
                clazz.value=""
                description.value=""

            }, modifier = Modifier.align(Alignment.CenterHorizontally)
            , enabled = clazz.value.isNotEmpty()&&description.value.isNotEmpty()) {
                Text(
                    text = if (editable) "Update" else "Submit",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}