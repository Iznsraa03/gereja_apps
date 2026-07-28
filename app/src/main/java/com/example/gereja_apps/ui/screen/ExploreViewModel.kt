package com.example.gereja_apps.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gereja_apps.data.remote.dto.ChurchDto
import com.example.gereja_apps.data.repository.ChurchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {
    private val repo = ChurchRepository

    private val _churches = MutableStateFlow<List<ChurchDto>>(emptyList())
    val churches: StateFlow<List<ChurchDto>> = _churches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        searchChurches()
    }

    fun searchChurches(query: String? = null, categoryId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val results = repo.getChurches(query, categoryId).getOrDefault(emptyList())
            _churches.value = results
            _isLoading.value = false
        }
    }
}
