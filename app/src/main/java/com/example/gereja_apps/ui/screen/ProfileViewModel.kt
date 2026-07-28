package com.example.gereja_apps.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gereja_apps.data.remote.dto.UserDto
import com.example.gereja_apps.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repo = AuthRepository

    val currentUser: StateFlow<UserDto?> = repo.currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        if (repo.hasToken()) {
            fetchProfile()
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repo.login(email, pass)
            if (result.isFailure) {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            repo.fetchProfile()
            _isLoading.value = false
        }
    }
}
