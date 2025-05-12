package com.example.schoolsareboring.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import com.example.schoolsareboring.models.ImageUploadResponse
import com.example.schoolsareboring.models.StudentData
import com.example.schoolsareboring.models.TeachersData
import com.example.schoolsareboring.models.UserData
import com.example.schoolsareboring.retrofit.ApiInstance
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class MainViewModel : ViewModel() {

    suspend fun uploadImage(context: Context, uri: Uri): Response<ImageUploadResponse> {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Cannot open URI")
            val bytes = inputStream.readBytes()
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), bytes)

            // ✅ Note the parameter name is "file", not "image"
            val body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile)

            return ApiInstance.api.uploadImage(body)
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error uploading image: ${e.message}")
            throw e
        }
    }



}