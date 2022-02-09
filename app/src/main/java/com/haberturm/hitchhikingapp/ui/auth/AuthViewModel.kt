package com.haberturm.hitchhikingapp.ui.auth

import androidx.lifecycle.ViewModel
import com.haberturm.hitchhikingapp.ui.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
) : ViewModel(), RouteNavigator by routeNavigator{
    fun onAuthClicked(){
        navigateToRoute(HomeRoute.get(0))
    }
}