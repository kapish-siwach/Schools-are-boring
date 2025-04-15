package com.example.schoolsareboring.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.R
import com.example.schoolsareboring.room.UserViewModel
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.models.StudentData
import java.util.*

class AddStudentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                AddStudent()
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val regNo = remember { mutableStateOf("") }
    val fatherName = remember { mutableStateOf("") }
    val motherName = remember { mutableStateOf("") }
    val dob = remember { mutableStateOf("") }
    val clazz = remember { mutableStateOf("") }
    val rollNo = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("Male") }
    val viewModel: UserViewModel = viewModel()
    val rollNoError = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val phoneError = remember { mutableStateOf("") }

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
                phoneError.value.isBlank()
    }

    fun isPhoneValid(phone: String): Boolean {
        val phoneRegex = "^[0-9]{10}$"
        return phone.matches(phoneRegex.toRegex())
    }

    // Validate email
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
        return email.matches(emailRegex.toRegex())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add new Student") },
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Student Details",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(10.dp))

                Column(Modifier.padding(start = 15.dp, end = 15.dp)) {


                    // Name
                    UserInputField(
                        label = "Full name *",
                        value = name,
                        endIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text
                    )

                    // Father name
                    UserInputField(
                        label = "Father's name *",
                        value = fatherName,
                        endIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text
                    )

                    // Mother name
                    UserInputField(
                        label = "Mother's name ",
                        value = motherName,
                        endIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text
                    )

                    // Phone
                    UserInputField(
                        label = "Phone *",
                        value = phone,
                        endIcon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone,
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
                        onValueChange = {
                            email.value = it
                            emailError.value = if (isEmailValid(it)) "" else "Invalid email format"
                        }
                    )
                    if (emailError.value.isNotEmpty()) {
                        Text(emailError.value, color = MaterialTheme.colorScheme.error)
                    }


//                    Registration Number
//                    UserInputField(
//                        label = "Registration no. *",
//                        value = regNo,
//                        endIcon = Icons.Default.Info,
//                        keyboardType = KeyboardType.Number
//                    )

                    // DOB Picker
                    DOBDatePicker(
                        dob = dob.value,
                        onDateSelected = { dob.value = it }
                    )

                    // Gender Radio Buttons
                    GenderRadioButtons(
                        selectedGender = gender.value,
                        onGenderSelected = { gender.value = it }
                    )

                    // Class Picker
                    ClassDropdownPicker(
                        selectedClass = clazz.value,
                        onClassSelected = { clazz.value = it }
                    )

                    // Roll Number
                    UserInputField(
                        label = "Roll no *",
                        value = rollNo,
                        endIcon = Icons.Default.AccountBox,
                        keyboardType = KeyboardType.Number
                    )
                    if (rollNoError.value.isNotEmpty()) {
                        Text(rollNoError.value, color = MaterialTheme.colorScheme.error)
                    }



                    Spacer(Modifier.height(10.dp))

//                    Buttons

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val isSubmitted = remember { mutableStateOf("Submit") }

                        ElevatedButton(
                            onClick = {
                                viewModel.isRollNoExist(clazz.value, rollNo.value) { rollNoExist ->
                                    if (rollNoExist) {
                                        rollNoError.value = "Roll no already exists in this class!"
                                    } else {
                                        rollNoError.value = ""
                                        val student = StudentData(
                                            name = name.value,
                                            fatherName = fatherName.value,
                                            motherName = motherName.value,
                                            phone = phone.value,
                                            email = email.value,
                                            dob = dob.value,
                                            gender = gender.value,
                                            clazz = clazz.value,
                                            rollNo = rollNo.value
                                        )
                                        viewModel.registerStudent(student)
                                        Toast.makeText(context, "Student Added.", Toast.LENGTH_SHORT).show()
                                        clearFields(
                                            name, email, fatherName, motherName,
                                            phone, dob, clazz, rollNo, gender
                                        )
                                        isSubmitted.value = "Add other"
                                    }
                                }
                            },
                            enabled = isFormValid,
                            modifier = Modifier.fillMaxWidth(),

                        ) {
                            Text(isSubmitted.value, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.padding(5.dp))
                        }
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
    gender: MutableState<String>
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
}

@Composable
fun UserInputField(
    label: String,
    value: MutableState<String>,
    endIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = value.value,
        onValueChange = {
            value.value = it
            onValueChange(it)
        },
        label = { Text(label) },
        trailingIcon = { Icon(endIcon, contentDescription = null) },
        singleLine = true,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}


@Composable
fun DOBDatePicker(
    dob: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Create the DatePickerDialog
    val datePickerDialog = remember {
        DatePickerDialog(context, { _: DatePicker, y: Int, m: Int, d: Int ->
            val formatted = String.format("%02d/%02d/%04d", d, m + 1, y)
            onDateSelected(formatted)
        }, year, month, day).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                datePickerDialog.show()
            }
    ) {
        TextField(
            value = dob,
            onValueChange = {},
            readOnly = true,
            label = { Text("Date of Birth *") },
            trailingIcon = {
                Icon(Icons.Default.Person, contentDescription = "DOB")
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = false
        )
    }
}

@Composable
fun GenderRadioButtons(selectedGender: String, onGenderSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(
                selected = selectedGender == "Male",
                onClick = { onGenderSelected("Male") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Male", modifier = Modifier.align(Alignment.CenterVertically))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(
                selected = selectedGender == "Female",
                onClick = { onGenderSelected("Female") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Female", modifier = Modifier.align(Alignment.CenterVertically))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(
                selected = selectedGender == "Other",
                onClick = { onGenderSelected("Other") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Other", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDropdownPicker(selectedClass: String, onClassSelected: (String) -> Unit) {
    val classOptions = listOf(
        "Class 1",
        "Class 2",
        "Class 3",
        "Class 4",
        "Class 5",
        "Class 6",
        "Class 7",
        "Class 8",
        "Class 9",
        "Class 10",
        "Class 11",
        "Class 12",
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = selectedClass,
            onValueChange = {},
            readOnly = true,
            label = { Text("Class *") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            classOptions.forEach { className ->
                DropdownMenuItem(
                    text = { Text(className) },
                    onClick = {
                        onClassSelected(className)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddStudent() {
    SchoolsAreBoringTheme {
        AddStudent()
    }
}
