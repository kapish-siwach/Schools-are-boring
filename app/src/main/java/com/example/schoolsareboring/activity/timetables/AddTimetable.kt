package com.example.schoolsareboring.activity.timetables

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.ClassDropdownPicker
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.UserInputField
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.SyllabusModal

class AddTimetable : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {

                    AddTimetableScreen()

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimetableScreen() {
    val context= LocalContext.current
    val clazz= remember { mutableStateOf("") }
    val fileUrl= remember { mutableStateOf("") }
    val preferenceManager= PreferenceManager(context)
    val viewModel: FirestoreViewModel = viewModel()

    Scaffold(   topBar = {
        TopAppBar(
            title = { Text("Add TimeTable") },
            navigationIcon = {
                IconButton(onClick = {(context as? Activity)?.finish()}) {
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft,"Back")
                }
            }
        )
    },) { innerPadding->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Column {
                ClassDropdownPicker(selectedClass = clazz.value,
                    onClassSelected = {clazz.value=it},
                    enabled = preferenceManager.getData("userType")=="admin")

                Spacer(modifier = Modifier.height(15.dp))

                UserInputField(label = "Paste drive url of file", value = fileUrl, onValueChange = {fileUrl.value = it}, endIcon = Icons.Default.Add)

                Spacer(modifier = Modifier.height(15.dp))

                ElevatedButton(onClick = {
                    val timeTable= SyllabusModal(
                        clazz=clazz.value,
                        fileUrl = fileUrl.value
                    )
                    viewModel.addTimetable(timeTable)
                    (context as Activity).finish()
                    Toast.makeText(context,"Syllabus added for ${clazz.value}", Toast.LENGTH_SHORT).show()
                }, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    enabled = fileUrl.value.trim().isNotEmpty() && clazz.value.trim().isNotEmpty()
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview8() {
    SchoolsAreBoringTheme {
        AddTimetableScreen()
    }
}