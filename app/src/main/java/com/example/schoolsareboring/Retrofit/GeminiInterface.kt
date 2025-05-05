package com.example.schoolsareboring.Retrofit

import com.example.schoolsareboring.models.GeminiRequest
import com.example.schoolsareboring.models.GeminiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiInterface {

 @POST("/v1beta/models/gemini-2.0-flash:generateContent")
 @Headers("Content-Type: application/json")
 fun generateContent(
     @Query("key") apiKey: String,
     @Body requestBody:GeminiRequest
 ):Call<GeminiResponse>
}