package com.example.schoolsareboring

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.firestore.FirestoreViewModel
import com.example.schoolsareboring.models.ChatItem
import com.example.schoolsareboring.models.SyllabusModal
import com.example.schoolsareboring.models.UserType
import io.noties.markwon.Markwon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
val currentDate: String =simpleDateFormat.format(Date())
const val BASE_URL="http://192.168.29.187:8000"

@Composable
fun UserInputField(
    label: String,
    value: MutableState<String>,
    endIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {},
    enabled : Boolean = true,
    modifier: Modifier = Modifier,
    singleLine:Boolean =true
) {
    OutlinedTextField(
        value = value.value,
        onValueChange = {
            value.value = it
            onValueChange(it)
        },
        label = { Text(label) },
        trailingIcon = { Icon(endIcon, contentDescription = label) },
        singleLine = singleLine,
        modifier = modifier
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
                Icon(Icons.Default.DateRange, contentDescription = "DOB")
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

@SuppressLint("DefaultLocale")
@Composable
fun DateSelector(
    sDate: String,
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
            val formatted = String.format("%02d-%02d-%04d", d, m + 1, y)
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
            value = sDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Date", color = Color.Black)},
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "date")
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
fun ClassFilter(
    selectedClass: String,
    onClassSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val classOptions = listOf("All", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.padding(horizontal = 5.dp).fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedClass,
            onValueChange = {},
            readOnly = true,
            label = { Text("Filter by Class") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            classOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onClassSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDropdownPicker(selectedClass: String, onClassSelected: (String) -> Unit, enabled: Boolean) {
    val classOptions = listOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
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
            label = { Text("Class") },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDetailsBottomSheet(
    scope: CoroutineScope,
    parent: String,
    sheetState: SheetState,
    showBottomSheet: MutableState<Boolean>
) {
    val context= LocalContext.current
    val clazz= remember { mutableStateOf("") }
    val fileUrl= remember { mutableStateOf("") }
    val session= PreferenceManager(context)
    val viewModel: FirestoreViewModel = viewModel()

    Column(Modifier.padding(10.dp)) {
        Column(modifier=Modifier.padding(horizontal = 10.dp, vertical = 10.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            ClassDropdownPicker(selectedClass = clazz.value,
                onClassSelected = {clazz.value=it},
                enabled = true)

            Spacer(modifier=Modifier.height(15.dp))

            UserInputField(label = "Paste drive url of file", value = fileUrl, onValueChange = {fileUrl.value = it}, endIcon = Icons.Default.Add)

            Spacer(modifier=Modifier.height(15.dp))

            ElevatedButton(onClick = {
                val data = SyllabusModal(
                    clazz=clazz.value,
                    fileUrl = fileUrl.value,
                    date = currentDate
                )
                when(parent){
                    "resultActivity" -> viewModel.addResults(data)
                    "syllabusActivity" -> viewModel.addSyllabus(data)
                    "timetableActivity" -> viewModel.addTimetable(data)
                }
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible){
                        showBottomSheet.value=false
                    }
                }
                Toast.makeText(context,"Added",Toast.LENGTH_SHORT).show()
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

@Composable
fun OutlinedFileCard(
    syllabus: SyllabusModal,
    session: PreferenceManager,
    viewModel: FirestoreViewModel,
    parent: String
) {

    val context = LocalContext.current
    Column {

        OutlinedCard (
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(syllabus.fileUrl))
                        try {
                            context.startActivity(intent)
                        }catch (e:Exception){
                            Toast.makeText(context,"Error opening file.",Toast.LENGTH_SHORT).show()
                        }

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Class ${syllabus.clazz}")

                Image(
                    painter = painterResource(id = R.drawable.book),
                    contentDescription = "download",
                    Modifier
                        .height(70.dp)
                        .padding(5.dp)
                )
            }
        }
        if (session.getData("userType")=="admin") {
            IconButton(
                onClick = {
                    when(parent){
                        "syllabus"-> viewModel.deleteSyllabus(syllabus.clazz)
                        "timetable"-> viewModel.deleteTimetable(syllabus.clazz)
                        "results"-> viewModel.deleteResults(syllabus.clazz)
                    }
                     },
                modifier = Modifier.offset( y = (-35).dp).size(35.dp)
                    .align(Alignment.End),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White
                )
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.size(35.dp).padding(2.dp),
                    colorResource(R.color.red)
                )
            }
        }
    }
}

