package com.example.schoolsareboring.activity.syllabus

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.ClassDropdownPicker
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.R
import com.example.schoolsareboring.UserInputField
import com.example.schoolsareboring.activity.syllabus.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.SyllabusModal

class AddSyllabus : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {

                    AddSyllabusScreen(modifier = Modifier)
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSyllabusScreen(modifier: Modifier = Modifier) {
    val context= LocalContext.current
    val clazz= remember { mutableStateOf("") }
    val fileUrl= remember { mutableStateOf("") }
    val preferenceManager=PreferenceManager(context)
    val viewModel:FirestoreViewModel= viewModel()



    Column(modifier.fillMaxSize()) {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text("Add Syllabus") },
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
        ){
            innerPadding->
            Column(modifier.padding(innerPadding).padding(horizontal = 10.dp, vertical = 10.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                ClassDropdownPicker(selectedClass = clazz.value,
                    onClassSelected = {clazz.value=it},
                    enabled = preferenceManager.getData("userType")=="admin")

                Spacer(modifier.height(15.dp))

                UserInputField(label = "Paste drive url of file", value = fileUrl, onValueChange = {fileUrl.value = it}, endIcon = Icons.Default.Add)

                Spacer(modifier.height(15.dp))

                ElevatedButton(onClick = {
                    val syllabus=SyllabusModal(
                          clazz=clazz.value,
                          fileUrl = fileUrl.value
                            )
                    viewModel.addSyllabus(syllabus)
                    (context as Activity).finish()
                    Toast.makeText(context,"Syllabus added for ${clazz.value}",Toast.LENGTH_SHORT).show()
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
fun GreetingPreview3() {
    SchoolsAreBoringTheme {
        AddSyllabusScreen()
    }
}