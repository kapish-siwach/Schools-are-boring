package com.example.schoolsareboring.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {
    private val dao = UserDatabase.getDatabase(application).userDao()
    private val repository = UserRepository(dao)


    val allUsers: Flow<List<UserData>> = repository.getAllUsers()

    fun registerUser(user: UserData) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun deleteUser(userData: UserData)=viewModelScope.launch {
        repository.delete(userData)
    }

    fun checkUserCredentials(email: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByCredentials(email, password)
            callback(user != null)
        }
    }

    fun getUserByEmail(email: String, callback: (Boolean) -> Unit){
        viewModelScope.launch {
            val user=repository.getUserByEmail(email)
            callback(user != null)
        }
    }

//    Students
    val allStudents: Flow<List<StudentData>> = repository.getAllStudents()

    fun registerStudent(studentData: StudentData){
        viewModelScope.launch {
            repository.insertStudent(studentData)
        }
    }

    fun isRollNoExist(clazz: String, rollNo: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = repository.isRollNoExist(clazz, rollNo)
            onResult(exists != null)
        }
    }

    fun checkStudentCredentials(stuEmail: String,stuRegNo: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val student = repository.checkStudentCredentials(stuEmail,stuRegNo)
            callback(student != null)
        }
    }

}