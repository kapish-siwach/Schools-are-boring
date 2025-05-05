package com.example.schoolsareboring.models

data class ChatItem(
    val text:String="",
    val userType:UserType
)
enum class UserType {
    USER, AI
}
