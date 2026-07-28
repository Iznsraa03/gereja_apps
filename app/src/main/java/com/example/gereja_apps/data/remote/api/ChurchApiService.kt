package com.example.gereja_apps.data.remote.api

import com.example.gereja_apps.data.remote.dto.ApiResponse
import com.example.gereja_apps.data.remote.dto.ChurchDetailDto
import com.example.gereja_apps.data.remote.dto.ChurchDto
import com.example.gereja_apps.data.remote.dto.PaginatedData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChurchApiService {
    @GET("churches")
    suspend fun getChurches(
        @Query("search") search: String? = null,
        @Query("category_id") categoryId: Int? = null,
        @Query("page") page: Int = 1
    ): ApiResponse<PaginatedData<ChurchDto>>

    @GET("churches/nearby")
    suspend fun getNearbyChurches(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("search") search: String? = null,
        @Query("category_id") categoryId: Int? = null,
        @Query("page") page: Int = 1
    ): ApiResponse<PaginatedData<ChurchDto>>

    @GET("churches/{slug}")
    suspend fun getChurchDetail(@Path("slug") slug: String): ApiResponse<ChurchDetailDto>
}
