package com.example.schoolsareboring.models


import java.io.Serializable

data class TeachersData(
    val id:String = "",
    val name:String= "",
    val email: String= "",
    val fatherName:String= "",
    val motherName:String= "",
    val dob:String= "",
    val subject:String= "",
    val uniqueCode:String= "",
    val phone: String= "",
    val gender:String= "",
    val imageUri: String? = null
):Serializable
