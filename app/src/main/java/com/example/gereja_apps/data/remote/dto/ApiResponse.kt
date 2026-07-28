package com.example.gereja_apps.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)

@JsonClass(generateAdapter = true)
data class PaginatedData<T>(
    val current_page: Int,
    val data: List<T>,
    val per_page: Int,
    val total: Int,
    val last_page: Int
)
