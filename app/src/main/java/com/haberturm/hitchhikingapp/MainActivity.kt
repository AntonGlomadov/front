package com.haberturm.hitchhikingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import com.haberturm.hitchhikingapp.ui.nav.ScreenHolder
import com.haberturm.hitchhikingapp.ui.theme.HitchHikingAppTheme
import com.haberturm.hitchhikingapp.ui.views.BottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            HitchHikingAppTheme {
                Scaffold(
                ) {
                    ScreenHolder(navController, it)
                }
            }
        }
    }
}

