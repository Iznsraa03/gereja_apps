package com.example.gereja_apps.data.remote.api

import com.example.gereja_apps.data.remote.dto.ApiResponse
import com.example.gereja_apps.data.remote.dto.ArticleDto
import com.example.gereja_apps.data.remote.dto.CategoryDto
import com.example.gereja_apps.data.remote.dto.PaginatedData
import retrofit2.http.GET
import retrofit2.http.Query

interface MasterApiService {
    @GET("categories")
    suspend fun getCategories(): ApiResponse<List<CategoryDto>>

    @GET("articles")
    suspend fun getArticles(@Query("page") page: Int = 1): ApiResponse<PaginatedData<ArticleDto>>
}
