package com.example.schoolsareboring.retrofit

import com.example.schoolsareboring.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance {
//    private const val BASE_URL="http://192.168.29.187:8000"

    val api:ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java )
    }
}