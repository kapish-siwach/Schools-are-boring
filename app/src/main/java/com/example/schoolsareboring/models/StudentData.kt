package com.example.schoolsareboring.models

import java.io.Serializable

data class StudentData (
     val regNo:String = "",
    val name:String = "",
    val email: String = "",
    val fatherName:String = "",
    val motherName:String = "",
    val dob:String = "",
    val clazz:String = "",
    val rollNo:String = "",
    val phone: String = "",
    val gender:String = "",
    val imageUri: String? = null
):Serializable

enum class AttendanceMark {
    PRESENT,
    ABSENT,
    LEAVE
}