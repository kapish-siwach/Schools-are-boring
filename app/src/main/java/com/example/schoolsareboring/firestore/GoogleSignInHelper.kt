package com.example.schoolsareboring.firestore

import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Composable
import com.example.schoolsareboring.PreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

fun handleGoogleSignInResult(
    result: ActivityResult,
    context: Context,
    firestoreViewModel: FirestoreViewModel,
    preferenceManager: PreferenceManager,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
    try {
        val account = task.getResult(ApiException::class.java)
        val email = account?.email ?: ""
        val name = account?.displayName ?: "Unnamed"

        firestoreViewModel.signInWithGoogle(email, name) { user ->
            if (user != null) {
                preferenceManager.setLoggedIn(true)
                preferenceManager.saveData("name", user.name ?: "")
                preferenceManager.saveData("email", user.email ?: "")
                preferenceManager.saveData("userType", user.role ?: "admin")
                onSuccess()
            } else {
                onFailure("Google sign-in failed.")
            }
        }
    } catch (e: Exception) {
        onFailure("Google sign-in error: ${e.message}")
    }
}


