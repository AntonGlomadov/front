package com.haberturm.hitchhikingapp

import android.app.Application
import com.haberturm.hitchhikingapp.ui.util.ModelPreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ModelPreferencesManager.with(this)
    }
}