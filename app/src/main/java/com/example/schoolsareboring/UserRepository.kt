package com.example.schoolsareboring

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
}