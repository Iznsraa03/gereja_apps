package com.example.gereja_apps.data.remote.api

import com.example.gereja_apps.data.remote.dto.ApiResponse
import com.example.gereja_apps.data.remote.dto.AuthResponseData
import com.example.gereja_apps.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("login")
    suspend fun login(@Body request: Map<String, String>): ApiResponse<AuthResponseData>

    @POST("register")
    suspend fun register(@Body request: Map<String, String>): ApiResponse<AuthResponseData>

    @POST("logout")
    suspend fun logout(): ApiResponse<Any>

    @GET("me")
    suspend fun getProfile(): ApiResponse<UserDto>
}
