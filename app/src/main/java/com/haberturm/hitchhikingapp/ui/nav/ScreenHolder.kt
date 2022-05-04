package com.haberturm.hitchhikingapp.ui.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.haberturm.hitchhikingapp.ui.auth.login.LoginRoute
import com.haberturm.hitchhikingapp.ui.auth.password.PasswordRoute
import com.haberturm.hitchhikingapp.ui.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.searchDirection.SearchDirectionRoute
import com.haberturm.hitchhikingapp.ui.auth.reg.RegRoute

@Composable
fun ScreenHolder(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = HomeRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        LoginRoute.composable(this, navHostController)
        HomeRoute.composable(this, navHostController)
        SearchDirectionRoute.composable(this, navHostController)
        PasswordRoute.composable(this, navHostController)
        RegRoute.composable(this, navHostController)
    }
}