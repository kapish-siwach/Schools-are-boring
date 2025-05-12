package com.example.schoolsareboring.firestore

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.schoolsareboring.models.AttendanceMark
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.SyllabusModal
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.models.UserData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
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

    var syllabus by mutableStateOf<List<SyllabusModal>>(emptyList())
        private set
    private val _syllabus = mutableStateListOf<SyllabusModal>()
    val allSyllabus:List<SyllabusModal> get() = _syllabus

    private var assignment by mutableStateOf<List<SyllabusModal>>(emptyList())
        private set
    private val _assignment = mutableStateListOf<SyllabusModal>()
    val allAssignment:List<SyllabusModal> get() = _assignment

    var timetable by mutableStateOf<List<SyllabusModal>>(emptyList())
        private set
    private val _timetable = mutableStateListOf<SyllabusModal>()
    val allTimetable:List<SyllabusModal> get() = _timetable

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
                        isLoading =false
                        Log.e("Firestore", "Listen failed.", error)
                        errorMessage = error.message
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        isLoading =false
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
                    isLoading =false
                    Log.d("Firestore", "Student added")
                }
                .addOnFailureListener {
                    isLoading =false
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

    fun checkStudentCredentials(email: String, phone: String, callback: (StudentData?) -> Unit) {
        db.collection("students")
            .whereEqualTo("email", email)
            .whereEqualTo("phone", phone)
            .get()
            .addOnSuccessListener { result ->
                val student = result.documents.firstOrNull()?.toObject(StudentData::class.java)
                callback(student)
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
            .set(mapOf("created" to true), SetOptions.merge())
            .addOnSuccessListener {

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
            .addOnFailureListener {
                errorMessage = it.message
                isLoading = false
            }
    }

    fun loadAllAttendance(callback: (Map<String, Map<String, AttendanceMark>>) -> Unit) {
        db.collection("attendance")
            .get()
            .addOnSuccessListener { dateSnapshots ->
                val result = mutableMapOf<String, MutableMap<String, AttendanceMark>>()
                val dates = dateSnapshots.documents.map { it.id }

                if (dates.isEmpty()) {
                    Log.d("AttendanceDebug", "No attendance dates found.")
                    callback(emptyMap())
                    return@addOnSuccessListener
                }

                var pending = dates.size

                for (date in dates) {
                    db.collection("attendance")
                        .document(date)
                        .collection("students")
                        .get()
                        .addOnSuccessListener { studentSnapshots ->
                            for (doc in studentSnapshots) {
                                val regNo = doc.id
                                val markStr = doc.getString("present") ?: continue
                                val mark = AttendanceMark.valueOf(markStr)

                                if (!result.containsKey(regNo)) {
                                    result[regNo] = mutableMapOf()
                                }
                                result[regNo]?.put(date, mark)
                            }
                        }
                        .addOnFailureListener {
                            Log.e("AttendanceDebug", "Failed to load data for $date", it)
                        }
                        .addOnCompleteListener {
                            pending--
                            if (pending == 0) {
                                Log.d("AttendanceDebug", "All data loaded. Total students with data: ${result.size}")
                                callback(result)
                            }
                        }
                }
            }
            .addOnFailureListener {
                Log.e("AttendanceDebug", "Failed to fetch attendance root", it)
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


//    Syllabus


    fun addSyllabus(syllabusModal: SyllabusModal){
        isLoading =true
        errorMessage =null
        db.collection("syllabus")
            .document(syllabusModal.clazz)
            .set(syllabusModal)
            .addOnSuccessListener {
                Log.d("Firestore", "Syllabus added ")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error adding syllabus", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    fun listenToSyllabus() {
        isLoading = true
        errorMessage = null

        db.collection("syllabus")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listen failed.", error)
                    errorMessage = error.message
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(SyllabusModal::class.java) }
                    syllabus = list
                    _syllabus.clear()
                    _syllabus.addAll(list)
                }
            }
    }

    fun deleteSyllabus(clazz: String) {
        isLoading = true
        errorMessage = null

        db.collection("syllabus")
            .document(clazz)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Syllabus deleted")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error deleting Syllabus", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    //    Assignments

    fun addAssignment(syllabusModal: SyllabusModal){
        isLoading =true
        errorMessage =null
        db.collection("assignments")
            .document(syllabusModal.clazz)
            .set(syllabusModal)
            .addOnSuccessListener {
                isLoading =false
                Log.d("Firestore", "Assignment added ")
            }
            .addOnFailureListener {
                isLoading =false
                Log.e("Firestore", "Error adding Assignment", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    fun listenToAssignment() {
        isLoading = true
        errorMessage = null

        db.collection("assignments")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    isLoading =false
                    Log.e("Firestore", "Listen failed.", error)
                    errorMessage = error.message
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    isLoading =false
                    val list = snapshot.documents.mapNotNull { it.toObject(SyllabusModal::class.java) }
                    assignment = list
                    _assignment.clear()
                    _assignment.addAll(list)
                }
            }
    }

    fun deleteAssignment(clazz: String) {
        isLoading = true
        errorMessage = null

        db.collection("assignments")
            .document(clazz)
            .delete()
            .addOnSuccessListener {
                isLoading =false
                Log.d("Firestore", "Assignment deleted")
            }
            .addOnFailureListener {
                isLoading =false
                Log.e("Firestore", "Error deleting Assignment", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }



//    Time Table

    fun addTimetable(syllabusModal: SyllabusModal){
        isLoading =true
        errorMessage =null
        db.collection("timetable")
            .document(syllabusModal.clazz)
            .set(syllabusModal)
            .addOnSuccessListener {
                Log.d("Firestore", "Timetable added ")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error adding Timetable", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }

    fun listenToTimetable() {
        isLoading = true
        errorMessage = null

        db.collection("timetable")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listen failed.", error)
                    errorMessage = error.message
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(SyllabusModal::class.java) }
                    timetable = list
                    _timetable.clear()
                    _timetable.addAll(list)
                }
            }
    }

    fun deleteTimetable(clazz: String) {
        isLoading = true
        errorMessage = null

        db.collection("timetable")
            .document(clazz)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Timetable deleted")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error deleting Timetable", it)
                errorMessage = it.message
            }
            .addOnCompleteListener {
                isLoading = false
            }
    }


}