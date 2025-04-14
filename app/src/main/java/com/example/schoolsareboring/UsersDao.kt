package com.example.schoolsareboring

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

}