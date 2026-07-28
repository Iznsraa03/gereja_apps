package com.example.gereja_apps.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gereja_apps.data.remote.dto.ChurchDetailDto
import com.example.gereja_apps.data.repository.ChurchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChurchDetailViewModel : ViewModel() {
    private val repo = ChurchRepository

    private val _churchDetail = MutableStateFlow<ChurchDetailDto?>(null)
    val churchDetail: StateFlow<ChurchDetailDto?> = _churchDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadChurch(slug: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val detail = repo.getChurchDetail(slug).getOrNull()
            _churchDetail.value = detail
            _isLoading.value = false
        }
    }
}
