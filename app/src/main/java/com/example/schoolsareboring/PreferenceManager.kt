package com.example.schoolsareboring

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
private val sharedPreferences:SharedPreferences=context.getSharedPreferences("User Data",Context.MODE_PRIVATE)

    fun saveData(key: String,value: String){
        val editor=sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }
    fun getData(key:String):String{
        return sharedPreferences.getString(key,"N/A")?: "N/A"
    }
    fun setLoggedIn(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", value)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun logOut(){
        val editor=sharedPreferences.edit()
//        editor.clear()
        setLoggedIn(false)
        editor.apply()
    }
}