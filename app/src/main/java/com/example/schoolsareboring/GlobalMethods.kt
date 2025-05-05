package com.example.schoolsareboring

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.schoolsareboring.models.ChatItem
import com.example.schoolsareboring.models.UserType
import io.noties.markwon.Markwon
import java.util.Calendar



@Composable
fun UserInputField(
    label: String,
    value: MutableState<String>,
    endIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {},
    enabled : Boolean = true
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        enabled = enabled,
        colors= OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.DarkGray,
            disabledBorderColor = Color.DarkGray,
            disabledLabelColor = Color.DarkGray,
            disabledTrailingIconColor = Color.DarkGray
        )
    )
}

@Composable
fun GenderRadioButtons(selectedGender: String, onGenderSelected: (String) -> Unit, enabled: Boolean) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(
                selected = selectedGender == "Male",
                onClick = { onGenderSelected("Male") },
                enabled = enabled
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
                onClick = { onGenderSelected("Female") },
                enabled = enabled
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
                onClick = { onGenderSelected("Other") },
                enabled = enabled
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Other", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDropDown(selectedSub: String, onSubSelected: (String) -> Unit, enabled: Boolean) {
    val classOptions = listOf(
        "Hindi",
        "English",
        "Maths",
        "Physics",
        "Biology",
        "Chemistry",
        "Physical Education",
        "Fine Arts",
        "Music",
        "History",
        "Geography",
        "Computer",
        "Sanskrit",
        "Economics",
        "Political Science",
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),

        ) {
        OutlinedTextField(
            value = selectedSub,
            onValueChange = {},
            readOnly = true,
            label = { Text("Subject *") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.DarkGray,
                disabledLabelColor = Color.DarkGray,
                disabledTextColor = Color.DarkGray
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            classOptions.forEach { className ->
                DropdownMenuItem(
                    text = { Text(className) },
                    onClick = {
                        onSubSelected(className)
                        expanded = false
                    },
                    enabled = enabled
                )
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

@SuppressLint("DefaultLocale")
@Composable
fun DOBDatePicker(
    dob: String,
    onDateSelected: (String) -> Unit,
    enabled: Boolean
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

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
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                datePickerDialog.show()
            }
    ) {
        OutlinedTextField(
            value = dob,
            onValueChange = {},
            readOnly = true,
            label = { Text("Date of Birth *", color = Color.Black)},
            trailingIcon = {
                Icon(Icons.Default.Person, contentDescription = "DOB")
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.DarkGray,
                disabledContainerColor = Color.Transparent,
                disabledTrailingIconColor = Color.DarkGray,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDropdownPicker(selectedClass: String, onClassSelected: (String) -> Unit, enabled: Boolean) {
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
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),

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
                .fillMaxWidth(),
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.DarkGray,
                disabledLabelColor = Color.DarkGray,
                disabledTextColor = Color.DarkGray
            )
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
                    },
                    enabled = enabled
                )
            }
        }
    }
}


@Composable
fun ChatBubble(item: ChatItem) {
    val backgroundColor = when (item.userType) {
        UserType.USER -> Color(0xFFD3F6B7)
        UserType.AI -> Color(0xFFE5E5E5)
    }
    val padding= if(item.userType == UserType.USER){
        PaddingValues(5.dp)
    }else{
        PaddingValues(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 25.dp)
    }
    val alignment = if (item.userType == UserType.USER) {
        Alignment.End
    } else {
        Alignment.Start
    }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .wrapContentWidth(alignment)
    ) {
        MarkdownTextView(item.text, backgroundColor)
//        Text(
//            text = item.text,
//            modifier = Modifier
//                .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
//                .padding(10.dp),
//            fontSize = 16.sp
//        )
    }
}

@Composable
fun MarkdownTextView(markdown: String, backgroundColor: Color) {
    val context = LocalContext.current
    val markwon = remember { Markwon.create(context) }

    AndroidView(
        factory = {
            TextView(context).apply {
                setTextColor(android.graphics.Color.BLACK)
                setPadding(24, 16, 24, 16)
            }
        },
        update = { textView ->
            markwon.setMarkdown(textView, markdown)
        },
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(4.dp)
    )
}

@Composable
fun MessageList(messages: List<ChatItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        items(messages) { item ->
            ChatBubble(item)
        }
    }
}
