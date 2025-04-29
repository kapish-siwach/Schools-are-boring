package com.example.schoolsareboring

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class PreferenceManager(context: Context) {
private val sharedPreferences:SharedPreferences=context.getSharedPreferences("User Data",Context.MODE_PRIVATE)

     val gson = Gson()

    fun saveData(key: String,value: String){
        val editor=sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }
    fun saveUserData(key: String,value: Any){
        val json = gson.toJson(value)
        saveData(key, json)
    }

    inline fun <reified T> getUserData(key: String): T? {
        val json = getData(key) ?: return null
        return gson.fromJson(json, T::class.java)
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