package com.example.schoolsareboring.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students_table")
data class StudentData(
    @PrimaryKey(autoGenerate = true) val regNo:Int = 0,
    val name:String,
    val email: String,
    val fatherName:String,
    val motherName:String,
    val dob:String,
    val clazz:String,
    val rollNo:String,
    val phone: String,
    val gender:String

)
