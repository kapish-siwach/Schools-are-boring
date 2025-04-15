package com.example.schoolsareboring.room

import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.UserData
import kotlinx.coroutines.flow.Flow

class UserRepository(private val usersDao: UsersDao) {
//    val allUsers:Flow<List<UserData>> = usersDao.getAllUsers()

    suspend fun insertUser(userData: UserData){
        usersDao.insert(userData)
    }

    suspend fun delete(userData: UserData){
        usersDao.delete(userData)
    }
    fun getAllUsers(): Flow<List<UserData>> = usersDao.getAllUsers()

    suspend fun getUserByCredentials(email: String, password: String): UserData? {
        return usersDao.getUserByCredentials(email, password)
    }

    suspend fun getUserByEmail(email: String): UserData?{
        return usersDao.getUserByEmail(email)
    }


//    Students

    fun getAllStudents():Flow<List<StudentData>> = usersDao.getAllStudents()

    suspend fun insertStudent(studentData: StudentData){
        usersDao.insertStudent(studentData)
    }
    suspend fun isRollNoExist(clazz: String, rollNo: String): StudentData? {
       return usersDao.getStudentByRollNo(clazz,rollNo)
    }

    suspend fun checkStudentCredentials( stuEmail: String,stuRegNo: String): StudentData? {
        return usersDao.checkStudentCredentials(stuEmail,stuRegNo)
    }
}