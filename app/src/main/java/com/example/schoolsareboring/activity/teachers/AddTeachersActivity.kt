package com.example.schoolsareboring.activity.teachers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.schoolsareboring.DOBDatePicker
import com.example.schoolsareboring.GenderRadioButtons
import com.example.schoolsareboring.R
import com.example.schoolsareboring.SubjectDropDown
import com.example.schoolsareboring.UserInputField
import com.example.schoolsareboring.activity.student.clearFields
import com.example.schoolsareboring.activity.teachers.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.isEmailValid
import com.example.schoolsareboring.isPhoneValid
import com.example.schoolsareboring.models.TeachersData

class AddTeachersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val teacherData = intent.getSerializableExtra("TeacherData") as? TeachersData
        val isEditable = intent.getBooleanExtra("nonEditable",true)
        val firstUser = intent.getBooleanExtra("firstTimeUser",false)
        setContent {
            SchoolsAreBoringTheme {
                    AddTeachersScreen(teacherData,isEditable,firstUser)
                }
            }
        }
    }

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeachersScreen(
    teachersData: TeachersData? = null,
    isEditable: Boolean,
    firstTimeUser: Boolean,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
) {
    val context = LocalContext.current
    val viewModel: FirestoreViewModel = viewModel()

    // Use isLoading from the ViewModel
    val isLoading by remember { mutableStateOf(viewModel.isLoading) }

    val name = remember { mutableStateOf(teachersData?.name ?: "") }
    val email = remember { mutableStateOf(teachersData?.email ?: "") }
    val fatherName = remember { mutableStateOf(teachersData?.fatherName ?: "") }
    val motherName = remember { mutableStateOf(teachersData?.motherName ?: "") }
    val dob = remember { mutableStateOf(teachersData?.dob ?: "") }
    val phone = remember { mutableStateOf(teachersData?.phone ?: "") }
    val gender = remember { mutableStateOf(teachersData?.gender ?: "") }
    val subject = remember { mutableStateOf(teachersData?.subject ?: "") }
    val uniqueCode = remember { mutableStateOf(teachersData?.uniqueCode ?: "") }
    val emailError = remember { mutableStateOf("") }
    val phoneError = remember { mutableStateOf("") }

    val selectedImageUri = remember { mutableStateOf(teachersData?.imageUri?.let { Uri.parse(it) }) }

    val isEditMode = teachersData != null
    val isSubmitted = remember { mutableStateOf(if (isEditMode) "Update" else "Submit") }
    val title = remember { mutableStateOf(if (!isEditable) "Profile" else if (isEditMode) "Update Teacher Details" else "Add New Teacher") }

    val isFormValid by derivedStateOf {
        name.value.isNotBlank() &&
                fatherName.value.isNotBlank() &&
                phone.value.isNotBlank() &&
                dob.value.isNotBlank() &&
                gender.value.isNotBlank() &&
                email.value.isNotBlank() &&
                emailError.value.isBlank() &&
                phoneError.value.isBlank() &&
                subject.value.isNotBlank() &&
                uniqueCode.value.isNotBlank() &&
                selectedImageUri.value != null
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri.value = it
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title.value) },
                navigationIcon = {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(
                            Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                FilledIconButton(
                    onClick = { if (isEditable) launcher.launch("image/*") },
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .background(color = Color.Transparent)
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (selectedImageUri.value != null) {
                        AsyncImage(
                            model = selectedImageUri.value,
                            contentDescription = "Selected Teacher Pic",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.social),
                            contentDescription = "Add Teacher Pic",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                        )
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    UserInputField("Full Name", name, Icons.Default.Person, KeyboardType.Text, enabled = isEditable)
                    UserInputField("Father's Name", fatherName, Icons.Default.Person, KeyboardType.Text, enabled = isEditable)
                    UserInputField("Mother's Name", motherName, Icons.Default.Person, KeyboardType.Text, enabled = isEditable)
                    UserInputField("Phone", phone, Icons.Default.Phone, KeyboardType.Phone, enabled = isEditable, onValueChange = {
                        phone.value = it
                        phoneError.value = if (isPhoneValid(it)) "" else "Invalid phone number"
                    })
                    if (phoneError.value.isNotEmpty()) {
                        Text(phoneError.value, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 10.dp))
                    }

                    UserInputField("Email", email, Icons.Default.Email, KeyboardType.Email, enabled = firstTimeUser, onValueChange = {
                        email.value = it
                        emailError.value = if (isEmailValid(it)) "" else "Invalid email format"
                    })
                    if (emailError.value.isNotEmpty()) {
                        Text(emailError.value, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 10.dp))
                    }

                    DOBDatePicker(
                        dob = dob.value,
                        onDateSelected = { dob.value = it },
                        enabled = isEditable
                    )

                    GenderRadioButtons(
                        selectedGender = gender.value,
                        onGenderSelected = { gender.value = it },
                        enabled = isEditable
                    )

                    SubjectDropDown(
                        selectedSub = subject.value,
                        onSubSelected = { subject.value = it },
                        enabled = isEditable
                    )

                    UserInputField("Unique Code", uniqueCode, Icons.Outlined.Lock, KeyboardType.Text, enabled = isEditable)
                }
                Spacer(Modifier.height(10.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isEditable) {

                        ElevatedButton(
                            onClick = {
                                val teacher = TeachersData(
                                    id = email.value,
                                    name = name.value,
                                    fatherName = fatherName.value,
                                    motherName = motherName.value,
                                    phone = phone.value,
                                    email = email.value,
                                    dob = dob.value,
                                    gender = gender.value,
                                    uniqueCode = uniqueCode.value,
                                    subject = subject.value,
                                    imageUri = selectedImageUri.value?.toString()
                                )

                                if (isEditMode) {
                                    viewModel.updateTeacher(teacher)
                                    Toast.makeText(context, "Teacher Updated.", Toast.LENGTH_SHORT).show()
                                    (context as Activity).finish()
                                } else {
                                    viewModel.addTeacher(teacher)
                                    Toast.makeText(context, "Teacher Added.", Toast.LENGTH_SHORT).show()
                                    clearFields(
                                        name, email, fatherName, motherName,
                                        phone, dob, subject, uniqueCode, gender,
                                        selectedImageUri
                                    )
                                    isSubmitted.value = "Add another"
                                    (context as Activity).finish()
                                }
                            },
                            enabled = isFormValid && !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(
                                    isSubmitted.value,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                    } else {
                        Text("Please contact admin to edit your profile.", color = Color.Red, fontSize = 18.sp, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, 5.dp))
                    }
                }
            }
        }
    }
}