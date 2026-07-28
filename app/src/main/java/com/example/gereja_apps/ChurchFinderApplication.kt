package com.example.gereja_apps

import android.app.Application
import com.example.gereja_apps.data.local.TokenManager
import com.example.gereja_apps.data.remote.NetworkClient
import com.example.gereja_apps.data.repository.AuthRepository

class ChurchFinderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // ponytail: initialize dependencies manually
        NetworkClient.init(this)
        
        val tokenManager = TokenManager(this)
        AuthRepository.init(tokenManager)
    }
}
