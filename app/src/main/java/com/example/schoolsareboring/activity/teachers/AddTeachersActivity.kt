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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.schoolsareboring.R
import com.example.schoolsareboring.activity.student.DOBDatePicker
import com.example.schoolsareboring.activity.student.GenderRadioButtons
import com.example.schoolsareboring.activity.student.UserInputField
import com.example.schoolsareboring.activity.student.clearFields
import com.example.schoolsareboring.activity.teachers.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.room.UserViewModel

class AddTeachersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val teacherData = intent.getSerializableExtra("TeacherData") as? TeachersData
        setContent {
            SchoolsAreBoringTheme {
                    AddTeachersScreen(teacherData)
                }
            }
        }
    }

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AddTeachersScreen(teachersData: TeachersData? =null ,modifier: Modifier = Modifier.verticalScroll(rememberScrollState())) {
    val context= LocalContext.current
    val name = remember { mutableStateOf(teachersData?.name?: "") }
    val email = remember { mutableStateOf( teachersData?.email?:"") }
    val fatherName = remember { mutableStateOf( teachersData?.fatherName?:"") }
    val motherName = remember { mutableStateOf( teachersData?.motherName?:"") }
    val dob = remember { mutableStateOf( teachersData?.dob?:"") }
    val phone = remember { mutableStateOf(teachersData?.phone?: "") }
    val gender = remember { mutableStateOf( teachersData?.gender?:"") }
    val subject = remember { mutableStateOf( teachersData?.subject?:"") }
    val uniqueCode = remember { mutableStateOf( teachersData?.uniqueCode?:"") }
    val emailError = remember { mutableStateOf("") }
    val phoneError = remember { mutableStateOf("") }
    val addImage =R.drawable.social
    val viewModel: UserViewModel = viewModel()

    val selectedImageUri = remember { mutableStateOf(teachersData?.imageUri?.let { Uri.parse(it) }) }

    val isEditMode = teachersData != null
    val isSubmitted = remember { mutableStateOf(if (isEditMode) "Update" else "Submit") }
    val title= remember { mutableStateOf( if(isEditMode) "Update Teacher Details" else "Add New Teacher") }


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
                uniqueCode.value.isNotBlank()&&
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
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 10.dp)
                            .clip(CircleShape)
                            .clickable { (context as Activity).finish() }
                    )
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
            Column (modifier=Modifier.fillMaxWidth()) {
                FilledIconButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .background(color = Color.Transparent)
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (selectedImageUri.value != null) {
                        AsyncImage(
                            model = selectedImageUri.value,
                            contentDescription = "Selected Student Pic",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp),
                            contentScale = ContentScale.Crop
                        )

                    } else {
                        Image(
                            painter = painterResource(id = addImage),
                            contentDescription = "Add Student Pic",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                        )
                    }
                }
                Column(modifier=Modifier.padding(horizontal = 10.dp)) {

                UserInputField("Full Name",name,Icons.Default.Person,KeyboardType.Text)

                UserInputField("Father's Name",fatherName,Icons.Default.Person,KeyboardType.Text)

                UserInputField("Mother's Name",motherName,Icons.Default.Person,KeyboardType.Text)

                UserInputField("Phone",phone,Icons.Default.Phone,KeyboardType.Phone,
                    onValueChange = {
                        phone.value = it
                        phoneError.value = if (isPhoneValid(it)) "" else "Invalid phone number"
                    }
                )
                if (phoneError.value.isNotEmpty()) {
                    Text(phoneError.value, color = MaterialTheme.colorScheme.error,modifier=Modifier.padding(start = 10.dp))
                }

                UserInputField("Email",email,Icons.Default.Email,KeyboardType.Email,onValueChange = {
                    email.value = it
                    emailError.value = if (isEmailValid(it)) "" else "Invalid email format"
                }
                )

                if (emailError.value.isNotEmpty()) {
                    Text(emailError.value, color = MaterialTheme.colorScheme.error,modifier=Modifier.padding(start = 10.dp))
                }

                DOBDatePicker(
                    dob = dob.value,
                    onDateSelected = { dob.value = it }
                )

                GenderRadioButtons(
                    selectedGender = gender.value,
                    onGenderSelected = { gender.value = it }
                )

                UserInputField("Subject",subject,Icons.Outlined.Info,KeyboardType.Text)

                UserInputField("Unique Code",uniqueCode,Icons.Outlined.Lock,KeyboardType.Text)
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {


                    ElevatedButton(
                        onClick = {
                                    val teacher = TeachersData(
                                        regNo = teachersData?.regNo ?: 0,
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
                                viewModel.registerTeacher(teacher)
                                Toast.makeText(context, "Teacher Added.", Toast.LENGTH_SHORT).show()
                                clearFields(
                                    name, email, fatherName, motherName,
                                    phone, dob, subject, uniqueCode, gender,
                                    selectedImageUri
                                )
                                isSubmitted.value = "Add other"
                            }

                            (context as Activity).finish()

                        },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            isSubmitted.value,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }

                }
            }
        }
    }
}

fun isPhoneValid(phone: String): Boolean {
    val phoneRegex = "^[0-9]{10}$"
    return phone.matches(phoneRegex.toRegex())
}

fun isEmailValid(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
    return email.matches(emailRegex.toRegex())
}

