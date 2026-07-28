package com.example.gereja_apps.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(
    val id: Int,
    val name: String,
    val slug: String,
    val icon_path: String? = null
)

@JsonClass(generateAdapter = true)
data class ArticleDto(
    val id: Int,
    val title: String,
    val slug: String,
    val excerpt: String?,
    val thumbnail_path: String?,
    val published_at: String?
)

@JsonClass(generateAdapter = true)
data class ChurchDto(
    val id: Int,
    val name: String,
    val slug: String,
    val address: String,
    val city: String,
    val latitude: String?,
    val longitude: String?,
    val distance: Double? = null,
    val verification_status: String?,
    val category: CategoryDto?
)

@JsonClass(generateAdapter = true)
data class WorshipScheduleDto(
    val id: Int,
    val title: String,
    val day_of_week: Int,
    val start_time: String,
    val end_time: String?,
    val preacher_name: String?
)

@JsonClass(generateAdapter = true)
data class FacilityDto(
    val id: Int,
    val name: String,
    val slug: String
)

@JsonClass(generateAdapter = true)
data class ChurchDetailDto(
    val id: Int,
    val name: String,
    val slug: String,
    val address: String,
    val city: String,
    val latitude: String?,
    val longitude: String?,
    val description: String?,
    val phone: String?,
    val website_url: String?,
    val main_image_path: String?,
    val verification_status: String?,
    val category: CategoryDto?,
    val schedules: List<WorshipScheduleDto>? = emptyList(),
    val facilities: List<FacilityDto>? = emptyList()
)
