package com.example.gereja_apps.data.repository

import com.example.gereja_apps.data.remote.NetworkClient
import com.example.gereja_apps.data.remote.api.ChurchApiService
import com.example.gereja_apps.data.remote.api.MasterApiService

object ChurchRepository {
    private val masterApi: MasterApiService by lazy { NetworkClient.createService() }
    private val churchApi: ChurchApiService by lazy { NetworkClient.createService() }

    suspend fun getCategories() = runCatching { masterApi.getCategories().data ?: emptyList() }.onFailure { it.printStackTrace() }
    
    suspend fun getArticles(page: Int = 1) = runCatching { masterApi.getArticles(page).data?.data ?: emptyList() }.onFailure { it.printStackTrace() }
    
    suspend fun getChurches(search: String? = null, categoryId: Int? = null) = 
        runCatching { churchApi.getChurches(search, categoryId).data?.data ?: emptyList() }.onFailure { it.printStackTrace() }
        
    suspend fun getNearbyChurches(lat: Double, lng: Double, search: String? = null, categoryId: Int? = null) = 
        runCatching { churchApi.getNearbyChurches(lat, lng, search, categoryId).data?.data ?: emptyList() }.onFailure { it.printStackTrace() }
        
    suspend fun getChurchDetail(slug: String) = 
        runCatching { churchApi.getChurchDetail(slug).data }.onFailure { it.printStackTrace() }
}
