package com.example.schoolsareboring.retrofit

import com.example.schoolsareboring.models.ImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("/upload/")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<ImageUploadResponse>

    @GET("/images/{image_name}")
    suspend fun getImageByName(
        @Path("image_name") imageName: String
    ): Response<ImageUploadResponse>
}