package com.haberturm.hitchhikingapp.ui.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.haberturm.hitchhikingapp.ui.screens.auth.login.LoginRoute
import com.haberturm.hitchhikingapp.ui.screens.auth.password.PasswordRoute
import com.haberturm.hitchhikingapp.ui.screens.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.screens.auth.reg.RegRoute
import com.haberturm.hitchhikingapp.ui.screens.message.MessageRoute
import com.haberturm.hitchhikingapp.ui.screens.profile.ProfileRoute

@Composable
fun ScreenHolder(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = LoginRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        LoginRoute.composable(this, navHostController)
        HomeRoute.composable(this, navHostController)
        PasswordRoute.composable(this, navHostController)
        RegRoute.composable(this, navHostController)
        ProfileRoute.composable(this, navHostController)
        MessageRoute.composable(this, navHostController)
    }
}