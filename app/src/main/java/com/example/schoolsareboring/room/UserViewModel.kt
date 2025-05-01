package com.example.schoolsareboring.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.SyllabusModal
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.models.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class UserViewModel(application: Application):AndroidViewModel(application) {
    private val dao = UserDatabase.getDatabase(application).userDao()
    private val repository = UserRepository(dao)

//
//    val allUsers: Flow<List<UserData>> = repository.getAllUsers()
//
//    fun registerUser(user: UserData) {
//        viewModelScope.launch {
//            repository.insertUser(user)
//        }
//    }
//
//    fun deleteUser(userData: UserData)=viewModelScope.launch {
//        repository.delete(userData)
//    }
//
//    fun checkUserCredentials(email: String, password: String, callback: (UserData?) -> Unit) {
//        viewModelScope.launch {
//            val user = repository.getUserByCredentials(email, password)
//            withContext(Dispatchers.Main) {
//                callback(user)
//            }
//        }
//    }
//
//    fun getUserByEmail(email: String, callback: (Boolean) -> Unit){
//        viewModelScope.launch {
//            val user=repository.getUserByEmail(email)
//            callback(user != null)
//        }
//    }

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

    fun checkStudentCredentials(stuEmail: String,stuRegNo: String, callback: (StudentData?) -> Unit) {
        viewModelScope.launch {
            val student = repository.checkStudentCredentials(stuEmail,stuRegNo)
            withContext(Dispatchers.Main){
                callback(student)
            }
        }
    }

    fun updateStudent(updatedStudent: StudentData) {
        viewModelScope.launch {
            repository.updateStudent(updatedStudent)
        }
    }

    fun deleteStudent(studentData: StudentData)=viewModelScope.launch {
        repository.deleteStudent(studentData)
    }

//    Teachers

//    val allTeachers: Flow<List<TeachersData>> = repository.getAllTeachers()
//
//   fun registerTeacher(teachersData: TeachersData) {
//       viewModelScope.launch {
//           repository.insertTeacher(teachersData)
//       }
//   }
//
//    fun updateTeacher(updatedTeacher: TeachersData){
//        viewModelScope.launch {
//            repository.updateTeacher(updatedTeacher)
//        }
//    }
//
//    fun checkTeacherCredential(email:String,code:String,callback: (TeachersData?) -> Unit){
//        viewModelScope.launch {
//           val teacher = repository.checkTeacherCredentials(email,code)
//            withContext(Dispatchers.Main) {
//                callback(teacher)
//            }
//        }
//    }
//
//    fun deleteTeacher(teachersData: TeachersData)=viewModelScope.launch {
//        repository.deleteTeacher(teachersData)
//    }

//    Syllabus

    fun insertSyllabus(syllabusModal: SyllabusModal){
        viewModelScope.launch {
            repository.insertSyllabus(syllabusModal)
        }
    }
    val getAllSyllabus:Flow<List<SyllabusModal>> = repository.getAllSyllabus()
}