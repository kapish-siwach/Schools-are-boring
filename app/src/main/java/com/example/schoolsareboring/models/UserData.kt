package com.example.schoolsareboring.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_data")
data class UserData(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    val name:String,
    val email:String,
    val password:String,
):Serializable
