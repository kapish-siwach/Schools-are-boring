package com.example.schoolsareboring.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "teachers_table")
data class TeachersData(
    @PrimaryKey(autoGenerate = true) val regNo:Int = 0,
    val name:String,
    val email: String,
    val fatherName:String,
    val motherName:String,
    val dob:String,
    val subject:String,
    val uniqueCode:String,
    val phone: String,
    val gender:String,
    val imageUri: String? = null
):Serializable
