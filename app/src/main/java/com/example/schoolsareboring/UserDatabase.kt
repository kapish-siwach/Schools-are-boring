package com.example.schoolsareboring

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.schoolsareboring.models.UserData

@Database(entities = [UserData::class], version = 1)
abstract class UserDatabase :RoomDatabase() {
    abstract fun userDao():UsersDao

    companion object{
        @Volatile private var INSTANCE: UserDatabase? =null

        fun getDatabase(context: Context):UserDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}