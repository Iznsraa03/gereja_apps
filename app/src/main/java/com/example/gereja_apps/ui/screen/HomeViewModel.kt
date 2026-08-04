package com.example.gereja_apps.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gereja_apps.data.remote.dto.ArticleDto
import com.example.gereja_apps.data.remote.dto.CategoryDto
import com.example.gereja_apps.data.remote.dto.ChurchDto
import com.example.gereja_apps.data.repository.ChurchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repo = ChurchRepository

    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val _nearbyChurches = MutableStateFlow<List<ChurchDto>>(emptyList())
    val nearbyChurches: StateFlow<List<ChurchDto>> = _nearbyChurches.asStateFlow()

    private val _articles = MutableStateFlow<List<ArticleDto>>(emptyList())
    val articles: StateFlow<List<ArticleDto>> = _articles.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _isLoading.value = true
            val cats = repo.getCategories().getOrDefault(emptyList())
            val arts = repo.getArticles().getOrDefault(emptyList())
            _categories.value = cats
            _articles.value = arts
            // ponytail: initial fallback fetch removed, LaunchedEffect handles it
            _isLoading.value = false
        }
    }

    // ponytail: Dynamic location & category fetcher
    fun fetchNearbyChurches(lat: Double, lng: Double, categoryId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val churches = repo.getNearbyChurches(lat, lng, categoryId = categoryId).getOrDefault(emptyList())
            _nearbyChurches.value = churches
            _isLoading.value = false
        }
    }
}
