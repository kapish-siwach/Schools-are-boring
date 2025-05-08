package com.example.schoolsareboring.activity.student

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.schoolsareboring.ClassDropdownPicker
import com.example.schoolsareboring.DOBDatePicker
import com.example.schoolsareboring.GenderRadioButtons
import com.example.schoolsareboring.R
import com.example.schoolsareboring.UserInputField
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.isEmailValid
import com.example.schoolsareboring.isPhoneValid
import com.example.schoolsareboring.models.StudentData
import java.util.*

class  AddStudentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val studentData = intent.getSerializableExtra("studentData") as? StudentData
        val editable=intent.getBooleanExtra("nonEditable",true)
        val firstT=intent.getBooleanExtra("firstT",false)
        setContent {
            SchoolsAreBoringTheme {
                AddStudent(studentData,editable,firstT)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudent(studentData: StudentData? = null, isEditable:Boolean,firstT:Boolean) {
    val context = LocalContext.current

    val name = remember { mutableStateOf(studentData?.name ?: "") }
    val email = remember { mutableStateOf(studentData?.email ?: "") }
    val fatherName = remember { mutableStateOf(studentData?.fatherName ?: "") }
    val motherName = remember { mutableStateOf(studentData?.motherName ?: "") }
    val dob = remember { mutableStateOf(studentData?.dob ?: "") }
    val clazz = remember { mutableStateOf(studentData?.clazz ?: "") }
    val rollNo = remember { mutableStateOf(studentData?.rollNo ?: "") }
    val phone = remember { mutableStateOf(studentData?.phone ?: "") }
    val gender = remember { mutableStateOf(studentData?.gender ?: "") }
    val addImage =R.drawable.social
    val selectedImageUri = remember { mutableStateOf(studentData?.imageUri?.let { Uri.parse(it) }) }

    val isEditMode = studentData != null
    val isSubmitted = remember { mutableStateOf(if (isEditMode) "Update" else "Submit") }
    val title= remember { mutableStateOf( if (!isEditable) "Profile" else if(isEditMode) "Update Student" else "Add New Student") }

//    val name = remember { mutableStateOf("") }
//    val email = remember { mutableStateOf("") }
//    val regNo = remember { mutableStateOf("") }
//    val fatherName = remember { mutableStateOf("") }
//    val motherName = remember { mutableStateOf("") }
//    val dob = remember { mutableStateOf("") }
//    val clazz = remember { mutableStateOf("") }
//    val rollNo = remember { mutableStateOf("") }
//    val phone = remember { mutableStateOf("") }
//    val gender = remember { mutableStateOf("Male") }

    val viewModel: FirestoreViewModel = viewModel()
    val rollNoError = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val phoneError = remember { mutableStateOf("") }


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


    val isFormValid by derivedStateOf {
        name.value.isNotBlank() &&
                fatherName.value.isNotBlank() &&
                phone.value.isNotBlank() &&
                dob.value.isNotBlank() &&
                gender.value.isNotBlank() &&
                clazz.value.isNotBlank() &&
                rollNo.value.isNotBlank() &&
                email.value.isNotBlank() &&
                emailError.value.isBlank() &&
                phoneError.value.isBlank() &&
                selectedImageUri.value!=null
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title.value)  },
                navigationIcon = {
                   IconButton(onClick = {(context as Activity).finish() }){
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
               /* Text(
                    text = "Please fill all details carefully!",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )*/

                Spacer(Modifier.height(10.dp))

                Column(Modifier.padding(start = 15.dp, end = 15.dp)) {

                    FilledIconButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .background(color = Color.Transparent)
                            .align(Alignment.CenterHorizontally),
                        enabled = isEditable
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

                    // Name
                    UserInputField(
                        label = "Full name *",
                        value = name,
                        endIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text,
                        enabled = isEditable
                    )

                    // Father name
                    UserInputField(
                        label = "Father's name *",
                        value = fatherName,
                        endIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text,
                        enabled = isEditable
                    )

                    // Mother name
                    UserInputField(
                        label = "Mother's name ",
                        value = motherName,
                        endIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text,
                        enabled = isEditable
                    )

                    // Phone
                    UserInputField(
                        label = "Phone *",
                        value = phone,
                        endIcon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone,
                        enabled = isEditable,
                        onValueChange = {
                            phone.value = it
                            phoneError.value = if (isPhoneValid(it)) "" else "Invalid phone number"
                        }
                    )
                    if (phoneError.value.isNotEmpty()) {
                        Text(phoneError.value, color = MaterialTheme.colorScheme.error)
                    }

                    // Email
                    UserInputField(
                        label = "Email *",
                        value = email,
                        endIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        enabled = !firstT,
                        onValueChange = {
                            email.value = it
                            emailError.value = if (isEmailValid(it)) "" else "Invalid email format"
                        }
                    )
                    if (!firstT){
                        Text("Email not editable later.", color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.padding(start = 10.dp), fontSize = 12.sp)
                    }
                    if (emailError.value.isNotEmpty()) {
                        Text(emailError.value, color = MaterialTheme.colorScheme.error)
                    }


                    // DOB Picker
                    DOBDatePicker(
                        dob = dob.value,
                        onDateSelected = { dob.value = it },
                        enabled=isEditable
                    )

                    // Gender Radio Buttons
                    GenderRadioButtons(
                        selectedGender = gender.value,
                        onGenderSelected = { gender.value = it },
                        enabled=isEditable
                    )

                    // Class Picker
                    ClassDropdownPicker(
                        selectedClass = clazz.value,
                        onClassSelected = { clazz.value = it },
                        enabled=isEditable
                    )

                    // Roll Number
                    UserInputField(
                        label = "Roll no *",
                        value = rollNo,
                        endIcon = Icons.Default.AccountBox,
                        keyboardType = KeyboardType.Number,
                        enabled = isEditable
                    )
                    if (rollNoError.value.isNotEmpty()) {
                        Text(rollNoError.value, color = MaterialTheme.colorScheme.error)
                    }



                    Spacer(Modifier.height(10.dp))

//                    Buttons

                    if(isEditable){
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {


                            ElevatedButton(
                                onClick = {
                                    viewModel.checkStudentRollNo(
                                        clazz.value,
                                        rollNo.value
                                    ) { rollNoExist ->

                                        val isChangingRollOrClass =
                                            studentData?.clazz != clazz.value || studentData.rollNo != rollNo.value
                                        if (!isEditMode && rollNoExist || (isEditMode && isChangingRollOrClass && rollNoExist)) {
                                            rollNoError.value =
                                                "Roll no already exists in this class!"
                                        } else {
                                            rollNoError.value = ""
                                            val student = StudentData(
                                                regNo = "",
                                                name = name.value,
                                                fatherName = fatherName.value,
                                                motherName = motherName.value,
                                                phone = phone.value,
                                                email = email.value,
                                                dob = dob.value,
                                                gender = gender.value,
                                                clazz = clazz.value,
                                                rollNo = rollNo.value,
                                                imageUri = selectedImageUri.value?.toString()
                                                    ?: studentData?.imageUri
                                            )

                                            if (isEditMode) {
                                                viewModel.addStudent(student)
                                                Toast.makeText(
                                                    context,
                                                    "Student Updated.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                (context as Activity).finish()
                                            } else {
                                                viewModel.addStudent(student)
                                                Toast.makeText(context,"Student Added.",Toast.LENGTH_SHORT).show()
                                                (context as Activity).finish()
                                                clearFields(
                                                    name,
                                                    email,
                                                    fatherName,
                                                    motherName,
                                                    phone,
                                                    dob,
                                                    clazz,
                                                    rollNo,
                                                    gender,
                                                    selectedImageUri
                                                )
                                                isSubmitted.value = "Add other"
                                            }
                                        }
                                    }
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
                    }else {
                        Text(
                            "Please contact admin to edit your profile.",
                            color = Color.Red,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 10.dp,
                                    5.dp
                                )
                        )
                    }
                }
            }
        }
    }
}

fun clearFields(
    name: MutableState<String>,
    email: MutableState<String>,
    fatherName: MutableState<String>,
    motherName: MutableState<String>,
    phone: MutableState<String>,
    dob: MutableState<String>,
    clazz: MutableState<String>,
    rollNo: MutableState<String>,
    gender: MutableState<String>,
    selectedImageUri: MutableState<Uri?>,

    ) {
    name.value = ""
    email.value = ""
    fatherName.value = ""
    motherName.value = ""
    phone.value = ""
    dob.value = ""
    clazz.value = ""
    rollNo.value = ""
    gender.value = ""
    selectedImageUri.value=null
}

