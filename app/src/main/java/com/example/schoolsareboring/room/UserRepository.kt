package com.example.schoolsareboring.room

import com.example.schoolsareboring.activity.teachers.Teachers
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.SyllabusModal
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.models.UserData
import kotlinx.coroutines.flow.Flow

class UserRepository(private val usersDao: UsersDao) {
//    val allUsers:Flow<List<UserData>> = usersDao.getAllUsers()

//    suspend fun insertUser(userData: UserData){
//        usersDao.insert(userData)
//    }
//
//    suspend fun delete(userData: UserData){
//        usersDao.delete(userData)
//    }
//    fun getAllUsers(): Flow<List<UserData>> = usersDao.getAllUsers()
//
//    suspend fun getUserByCredentials(email: String, password: String): UserData? {
//        return usersDao.getUserByCredentials(email, password)
//    }
//
//    suspend fun getUserByEmail(email: String): UserData?{
//        return usersDao.getUserByEmail(email)
//    }


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

    suspend fun updateStudent(student: StudentData) {
        usersDao.updateStudent(student)
    }

    suspend fun deleteStudent(student: StudentData){
        usersDao.deleteStudent(student)
    }

//    Teachers

    fun getAllTeachers():Flow<List<TeachersData>> = usersDao.getAllTeachers()

    suspend fun checkTeacherCredentials(email:String,code:String):TeachersData?{
        return usersDao.checkTeacherCredentials(email,code)
    }

    suspend fun deleteTeacher(teachers: TeachersData){
        usersDao.deleteTeacher(teachers)
    }
    suspend fun insertTeacher(teachers: TeachersData){
        usersDao.insertTeacher(teachers)
    }

    suspend fun updateTeacher(teachers: TeachersData){
        usersDao.updateTeacher(teachers)
    }

//  Syllabus
    suspend fun insertSyllabus(syllabusModal: SyllabusModal){
        usersDao.insertSyllabus(syllabusModal)
    }

    fun getAllSyllabus():Flow<List<SyllabusModal>> = usersDao.getAllSyllabus()
}