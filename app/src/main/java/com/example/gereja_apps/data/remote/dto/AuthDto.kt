package com.example.gereja_apps.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val role: String,
    val is_active: Boolean,
    val avatar_path: String?,
    val created_at: String?,
    val updated_at: String?,
    val last_login_at: String?
)

@JsonClass(generateAdapter = true)
data class AuthResponseData(
    val user: UserDto,
    val token: String?
)
