package com.example.schoolsareboring.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolsareboring.retrofit.GeminiInstance
import com.example.schoolsareboring.models.Content
import com.example.schoolsareboring.models.GeminiRequest
import com.example.schoolsareboring.models.GeminiResponse
import com.example.schoolsareboring.models.Part
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeminiViewModel : ViewModel() {
    private val _geminiResponse = MutableLiveData<GeminiResponse>()
    val geminiResponse: LiveData<GeminiResponse> get() = _geminiResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun generateContent(apiKey: String, text: String) {
        val requestBody = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = text))))
        )
        GeminiInstance.apiService.generateContent(apiKey, requestBody)
            .enqueue(object : Callback<GeminiResponse> {
                override fun onResponse(
                    call: Call<GeminiResponse>,
                    response: Response<GeminiResponse>
                ) {
                    if (response.isSuccessful) {
                        _geminiResponse.postValue(response.body())
                    } else {
                        _error.postValue("Error: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                    _error.postValue("Failure: ${t.message}")
                }
            })
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun clearResponse() {
        _geminiResponse.value = null
        _error.value = null
    }

}

