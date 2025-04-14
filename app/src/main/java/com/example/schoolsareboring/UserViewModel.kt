package com.example.schoolsareboring

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolsareboring.models.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {
    private val dao = UserDatabase.getDatabase(application).userDao()
    private val repository = UserRepository(dao)

//    val allUsers:StateFlow<List<UserData>> = reposatory.allUsers.stateIn(
//        viewModelScope,
//        SharingStarted.Lazily,
//        emptyList()
//    )
    val allUsers: Flow<List<UserData>> = repository.getAllUsers()

    fun registerUser(user: UserData) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun deleteUser(userData: UserData)=viewModelScope.launch {
        repository.delete(userData)
    }

    fun checkUserCredentials(email: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByCredentials(email, password)
            callback(user != null)
        }
    }


}