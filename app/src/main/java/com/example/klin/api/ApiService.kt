package com.example.klin

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("/signin")
    suspend fun signin(
        @Header("x-api-key") apiKey: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @POST("/signup")
    @Multipart
    suspend fun signup(
        @Header("x-api-key") apiKey: String,
        @Part("users_name") name: RequestBody,
        @Part("users_email") email: RequestBody,
        @Part("users_password") password: RequestBody,
        @Part("users_phone") phone: RequestBody,
        @Part("users_role") role: RequestBody,
        @Part image: MultipartBody.Part? // Accept MultipartBody.Part for the image
    ): Response<JsonObject>

    @POST("/predict")
    @Multipart
    suspend fun predict(
        @Header("x-api-key") apiKey: String,
        @Part image: MultipartBody.Part? // Accept MultipartBody.Part for the image
    ): Response<JsonObject>

    @GET("/users/{id}")
    suspend fun getUserByID(
        @Header("x-api-key") apiKey: String,
        @Path("id") id: String
    ): Response<JsonObject>

}