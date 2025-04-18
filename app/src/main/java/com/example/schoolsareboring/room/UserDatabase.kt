package com.example.schoolsareboring.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.UserData

@Database(entities = [UserData::class,StudentData::class], version = 2)
abstract class UserDatabase :RoomDatabase() {
    abstract fun userDao(): UsersDao

    companion object{
        @Volatile private var INSTANCE: UserDatabase? =null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_db"
                ).fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}