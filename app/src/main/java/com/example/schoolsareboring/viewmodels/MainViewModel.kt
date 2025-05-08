package com.example.schoolsareboring.viewmodels

import androidx.lifecycle.ViewModel
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.models.UserData

class MainViewModel : ViewModel() {
    var userType:String=""
    var userName:String=""
    var studentData:StudentData?=null
    var teacherData:TeachersData?=null
    var adminData:UserData?=null
}