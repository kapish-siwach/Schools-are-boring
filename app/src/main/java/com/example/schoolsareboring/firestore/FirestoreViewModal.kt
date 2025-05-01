package com.example.schoolsareboring.firestore

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.schoolsareboring.models.UserData
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var admins by mutableStateOf<List<UserData>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun addAdmin(userData: UserData) {
        isLoading = true
        errorMessage = null

        db.collection("admins")
            .document((userData.id ?: "").toString())
            .set(userData)
            .addOnSuccessListener {
                Log.d("Firestore", "Admin added")
                getAdmins()
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error adding admin", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }


    fun getAdmins() {
        isLoading = true
        errorMessage = null

        db.collection("admins")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.toObject(UserData::class.java) }
                admins = list
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error fetching admins", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    fun checkAdminEmailExists(email: String, callback: (Boolean) -> Unit) {
        db.collection("admins")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->
                callback(!result.isEmpty)
            }
            .addOnFailureListener {
                Log.e("Firestore", "Email check failed", it)
                callback(false)
            }
    }

    fun getAdminByEmailAndPassword(email: String, password: String, callback: (UserData?) -> Unit) {
        db.collection("admins")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { result ->
                val user = result.documents.firstOrNull()?.toObject(UserData::class.java)
                callback(user)
            }
            .addOnFailureListener {
                Log.e("Firestore", "Login error", it)
                callback(null)
            }
    }

    fun signInWithGoogle(accountEmail: String, displayName: String, onResult: (UserData?) -> Unit) {
        val userCollection = db.collection("users")

        userCollection.whereEqualTo("email", accountEmail).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    // User exists → log them in
                    val existingUser = result.documents[0].toObject(UserData::class.java)
                    onResult(existingUser)
                } else {
                    // User does NOT exist → create new user
                    val newUser = UserData(
                        id = accountEmail,
                        name = displayName,
                        email = accountEmail,
                        role = "admin"
                    )
                    userCollection.add(newUser)
                        .addOnSuccessListener {
                            onResult(newUser)
                        }
                        .addOnFailureListener {
                            onResult(null)
                        }
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

}
