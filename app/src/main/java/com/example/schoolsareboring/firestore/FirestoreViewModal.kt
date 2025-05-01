package com.example.schoolsareboring.firestore

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.models.UserData
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var admins by mutableStateOf<List<UserData>>(emptyList())
        private set

    var teachers by mutableStateOf<List<TeachersData>>(emptyList())
        private set

    private val _teachers = mutableStateListOf<TeachersData>()
    val allTeachers: List<TeachersData> get() = _teachers

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Add Admin
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

    // Fetch Admins
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

    // Check if Admin Email Exists
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

    // Get Admin by Email and Password
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

    // Sign-in with Google (Admin)
    fun signInWithGoogle(accountEmail: String, displayName: String, onResult: (UserData?) -> Unit) {
        val userCollection = db.collection("admins")

        userCollection.whereEqualTo("email", accountEmail).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val existingUser = result.documents[0].toObject(UserData::class.java)
                    onResult(existingUser)
                } else {
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

    // Add Teacher
    fun addTeacher(teacher: TeachersData) {
        isLoading = true
        errorMessage = null

        db.collection("teachers")
            .document((teacher.id ?: "").toString())
            .set(teacher)
            .addOnSuccessListener {
                Log.d("Firestore", "Teacher added")
                // Optional: You could still use this to refresh the teachers
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error adding Teacher", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    // Fetch Teachers with Real-Time Updates
    fun listenToTeachers() {
        isLoading = true
        errorMessage = null

        // Real-time updates using snapshot listener
        db.collection("teachers")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listen failed.", error)
                    errorMessage = error.message
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(TeachersData::class.java) }
                    teachers = list
                    _teachers.clear()
                    _teachers.addAll(list)
                }
            }
    }

    fun getTeacherByEmailCode(email: String, code: String, callback: (TeachersData?) -> Unit) {
        db.collection("teachers")
            .whereEqualTo("email", email)
            .whereEqualTo("uniqueCode", code)
            .get()
            .addOnSuccessListener { result ->
                val teacher = result.documents.firstOrNull()?.toObject(TeachersData::class.java)
                callback(teacher)
            }
            .addOnFailureListener {
                Log.e("Firestore", "Login error", it)
                callback(null)
            }
    }

    // Update Teacher
    fun updateTeacher(teacher: TeachersData) {
        isLoading = true
        errorMessage = null

        db.collection("teachers")
            .document((teacher.id ?: "").toString())
            .set(teacher, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Firestore", "Teacher updated")

            }
            .addOnFailureListener {
                Log.e("Firestore", "Error updating teacher", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    // Delete Teacher
    fun deleteTeacher(teacherId: String) {
        isLoading = true
        errorMessage = null

        db.collection("teachers")
            .document(teacherId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Teacher deleted")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error deleting teacher", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }


}