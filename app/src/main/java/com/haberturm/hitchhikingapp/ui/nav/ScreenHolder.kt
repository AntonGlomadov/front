package com. haberturm.hitchhikingapp.ui.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.haberturm.hitchhikingapp.ui.auth.AuthRoute
import com.haberturm.hitchhikingapp.ui.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.searchDirection.SearchDirectionRoute

@Composable
fun ScreenHolder(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = AuthRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        AuthRoute.composable(this, navHostController)
        HomeRoute.composable(this, navHostController)
        SearchDirectionRoute.composable(this, navHostController)
    }
}