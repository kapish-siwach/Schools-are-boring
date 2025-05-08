package com.example.schoolsareboring.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GeminiInstance {

    private val retrofit:Retrofit by lazy {
        val okHttpClient =OkHttpClient.Builder().callTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .connectTimeout(60,TimeUnit.SECONDS).build()

        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .client(okHttpClient)

            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService:GeminiInterface by lazy {
        retrofit.create(GeminiInterface::class.java)
    }
}