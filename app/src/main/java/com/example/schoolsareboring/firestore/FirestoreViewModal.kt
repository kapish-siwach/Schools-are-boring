package com.example.schoolsareboring.firestore

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.schoolsareboring.models.AttendanceMark
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.models.UserData
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FirestoreViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var admins by mutableStateOf<List<UserData>>(emptyList())
        private set

    var teachers by mutableStateOf<List<TeachersData>>(emptyList())
        private set
    private val _teachers = mutableStateListOf<TeachersData>()
    val allTeachers: List<TeachersData> get() = _teachers

    var students by mutableStateOf<List<StudentData>>(emptyList())
        private set

    private val _students= mutableStateListOf<StudentData>()
    val allStudents:List<StudentData> get() = _students

    val attendanceSelections = mutableStateMapOf<String, AttendanceMark>()


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

//    Fetch Students

    fun listenToStudents() {
            isLoading = true
            errorMessage = null

            db.collection("students")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("Firestore", "Listen failed.", error)
                        errorMessage = error.message
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val list = snapshot.documents.mapNotNull { it.toObject(StudentData::class.java) }
                        students = list
                        _students.clear()
                        _students.addAll(list)
                    }
                }
        }

    fun addStudent(student: StudentData) {
            isLoading = true
            errorMessage = null

            val regNos = student.clazz + student.rollNo  // create unique regNo
            val newStudent = student.copy(regNo = regNos)

            db.collection("students")
                .document(regNos)
                .set(newStudent)
                .addOnSuccessListener {
                    Log.d("Firestore", "Student added")
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error adding Student", it)
                    errorMessage = it.message
                }
                .addOnCompleteListener {
                    isLoading = false
                }
        }

    fun checkStudentRollNo(clazz: String, roll: String, callback: (Boolean) -> Unit) {
        db.collection("students")
            .whereEqualTo("clazz", clazz)
            .whereEqualTo("rollNo", roll)
            .get()
            .addOnSuccessListener { result ->
                callback(!result.isEmpty)
            }
            .addOnFailureListener {
                Log.e("Firestore", "Roll no check failed", it)
                callback(false)
            }
    }

    fun deleteStudent(studentId: String) {
        isLoading = true
        errorMessage = null

        db.collection("students")
            .document(studentId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "student deleted")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error deleting student", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    fun checkStudentCredentials(email: String, phone: String, callback: (TeachersData?) -> Unit) {
        db.collection("students")
            .whereEqualTo("email", email)
            .whereEqualTo("phone", phone)
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

    fun markStudentAttendance(regNo: String, mark: AttendanceMark) {
        isLoading = true
        errorMessage = null

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val attendanceData = hashMapOf(
            "present" to mark.name,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("attendance")
            .document(date)
            .collection("students")
            .document(regNo)
            .set(attendanceData)
            .addOnSuccessListener {
                attendanceSelections[regNo] = mark
            }
            .addOnFailureListener {
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    fun getAttendanceByDate(date: String, callback: (Map<String, Boolean>) -> Unit) {
        db.collection("attendance")
            .document(date)
            .collection("students")
            .get()
            .addOnSuccessListener { snapshot ->
                val result = snapshot.documents.associate {
                    it.id to (it.getBoolean("present") ?: false)
                }
                callback(result)
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error fetching attendance", it)
                callback(emptyMap())
            }
    }

    fun loadAttendanceForDate(date: String) {
        db.collection("attendance")
            .document(date)
            .collection("students")
            .get()
            .addOnSuccessListener { snapshot ->
                attendanceSelections.clear()
                for (doc in snapshot.documents) {
                    val regNo = doc.id
                    val markStr = doc.getString("present") ?: continue
                    val mark = AttendanceMark.valueOf(markStr)
                    attendanceSelections[regNo] = mark
                }
            }
    }

}