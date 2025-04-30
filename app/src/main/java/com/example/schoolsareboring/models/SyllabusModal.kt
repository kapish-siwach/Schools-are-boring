package com.example.schoolsareboring.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "syllabus_table")
data class SyllabusModal(
   @PrimaryKey val clazz:String,
    val fileUri:String
)
