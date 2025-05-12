package com.example.schoolsareboring.activity.myai

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.schoolsareboring.viewmodels.GeminiViewModel
import com.example.schoolsareboring.MessageList
import com.example.schoolsareboring.activity.myai.ui.theme.SchoolsAreBoringTheme
import com.example.schoolsareboring.models.ChatItem
import com.example.schoolsareboring.models.UserType

class MyAi : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                    MyAiScreen(
                        modifier = Modifier
                    )
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAiScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val userInput = remember { mutableStateOf("") }
    val response = remember { mutableStateOf("") }
    val placeholderr = remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    var chatItems by remember { mutableStateOf(listOf<ChatItem>()) }
    val viewModel: GeminiViewModel = viewModel()

    val geminiResponse by viewModel.geminiResponse.observeAsState()
    val error by viewModel.error.observeAsState()

    geminiResponse?.let { responseText->
        isThinking=true
        val text =  responseText.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No Response"
        chatItems = chatItems.dropLast(1) + ChatItem(text,UserType.AI)
        isThinking=false
        viewModel.clearResponse()
    }
    error?.let {errorMsg->
        isThinking=true
        chatItems=chatItems.dropLast(1)+ChatItem("Error $errorMsg",UserType.AI)
        isThinking=false
        viewModel.clearResponse()
    }

        Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text("My AI (Temporary Chats)") },
                    navigationIcon = {
                        IconButton(onClick = {(context as? Activity)?.finish()}) {
                            Icon(
                                Icons.AutoMirrored.Default.KeyboardArrowLeft,
                                contentDescription = "Back",
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        if (isThinking){
                            placeholderr.value="Thinking..."
                        }else {
                            placeholderr.value = "Ask Anything"
                        }

                        OutlinedTextField(
                            value = userInput.value,
                            onValueChange = {userInput.value=it},
                            placeholder = {Text(text = placeholderr.value, fontSize = 16.sp)},
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.weight(0.2f),
                        )
                        IconButton(onClick = {
                            val inputText =userInput.value.trim()
                            if (inputText.isNotEmpty()){
                            isThinking=true
                                chatItems += ChatItem(inputText,UserType.USER)
                                userInput.value=""
                                chatItems += ChatItem("Thinking...", UserType.AI)

                                viewModel.generateContent(
                                    apiKey = "AIzaSyCm7SKfpTE6JtUESP-UPdVBePYa0tXkbOw",
                                    text = inputText
                                )
                              }
                        }, enabled = userInput.value.trim().isNotEmpty() && !isThinking) {
                            Icon(Icons.AutoMirrored.Default.Send, contentDescription = "send")
                        }
                    }
                }
            }, modifier = Modifier.fillMaxWidth()/*.align(Alignment.CenterHorizontally)*/,
            content = {
                    innerPadding->
                Column(modifier.padding(innerPadding).fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                    MessageList(messages = chatItems)
                }
            })
    }



@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    SchoolsAreBoringTheme {
        MyAiScreen()
    }
}