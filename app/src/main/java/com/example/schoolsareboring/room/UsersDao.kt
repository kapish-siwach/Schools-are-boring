package com.example.schoolsareboring.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.SyllabusModal
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.models.UserData
import kotlinx.coroutines.flow.Flow


@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserData)

    @Query("SELECT * FROM user_data")
    fun getAllUsers(): Flow<List<UserData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userData: UserData)

    @Delete
    suspend fun delete(userData: UserData)

    @Query("SELECT * FROM user_data WHERE email = :email AND password = :password")
    suspend fun getUserByCredentials(email: String, password: String): UserData?

    @Query("SELECT * FROM user_data WHERE email= :email")
    suspend fun getUserByEmail(email: String): UserData?

//    Students

    @Query("SELECT * FROM students_table")
    fun getAllStudents(): Flow<List<StudentData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(studentData: StudentData)

    @Delete
    suspend fun deleteStudent(student: StudentData)

    @Query("SELECT * FROM students_table WHERE clazz = :clazz AND rollNo = :rollNo")
    suspend fun getStudentByRollNo(clazz: String, rollNo: String): StudentData?

    @Query("SELECT * FROM students_table WHERE  regNo = :stuRegNo AND email = :stuEmail")
    suspend fun checkStudentCredentials(stuEmail: String,stuRegNo: String): StudentData?

    @Update
    suspend fun updateStudent(student: StudentData)

//      Teachers

    @Query("SELECT * FROM teachers_table")
    fun getAllTeachers(): Flow<List<TeachersData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teachersData: TeachersData)

    @Update
    suspend fun updateTeacher(teachersData: TeachersData)

    @Query("SELECT * FROM teachers_table WHERE email = :email AND uniqueCode = :code ")
    suspend fun checkTeacherCredentials(email: String,code: String): TeachersData?

//    Syllabus

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyllabus(syllabusModal: SyllabusModal)

    @Query("SELECT * FROM syllabus_table")
    fun getAllSyllabus(): Flow<List<SyllabusModal>>
}