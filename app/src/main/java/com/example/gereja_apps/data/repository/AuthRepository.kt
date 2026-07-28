package com.example.gereja_apps.data.repository

import com.example.gereja_apps.data.local.TokenManager
import com.example.gereja_apps.data.remote.NetworkClient
import com.example.gereja_apps.data.remote.api.AuthApiService
import com.example.gereja_apps.data.remote.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ponytail: Simple singleton repository
object AuthRepository {
    private val api: AuthApiService by lazy { NetworkClient.createService() }
    
    // In-memory state
    private val _currentUser = MutableStateFlow<UserDto?>(null)
    val currentUser: StateFlow<UserDto?> = _currentUser.asStateFlow()
    
    private lateinit var tokenManager: TokenManager

    fun init(tokenManager: TokenManager) {
        this.tokenManager = tokenManager
    }
    
    fun hasToken(): Boolean = tokenManager.getToken() != null

    suspend fun login(email: String, password: String): Result<UserDto> {
        return try {
            val req = mapOf("email" to email, "password" to password)
            val res = api.login(req)
            if (res.success && res.data != null) {
                res.data.token?.let { tokenManager.saveToken(it) }
                _currentUser.value = res.data.user
                Result.success(res.data.user)
            } else {
                Result.failure(Exception(res.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchProfile(): Result<UserDto> {
        return try {
            val res = api.getProfile()
            if (res.success && res.data != null) {
                _currentUser.value = res.data
                Result.success(res.data)
            } else {
                Result.failure(Exception(res.message ?: "Failed to fetch profile"))
            }
        } catch (e: Exception) {
            if (e is retrofit2.HttpException && e.code() == 401) {
                logout() // Auto logout on 401
            }
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            if (tokenManager.getToken() != null) {
                api.logout()
            }
            clearSession()
            Result.success(Unit)
        } catch (e: Exception) {
            clearSession()
            Result.success(Unit) // Force logout locally anyway
        }
    }
    
    private fun clearSession() {
        tokenManager.clearToken()
        _currentUser.value = null
    }
}
