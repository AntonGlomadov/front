package com.haberturm.hitchhikingapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.nav.ScreenHolder
import com.haberturm.hitchhikingapp.ui.screens.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.theme.HitchHikingAppTheme
import com.haberturm.hitchhikingapp.ui.util.Constants
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_SHOW_PROPER_MAP_STATE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent?.action == ACTION_SHOW_PROPER_MAP_STATE){
           //TODO implement navigation with user mode
        }
    }
}

