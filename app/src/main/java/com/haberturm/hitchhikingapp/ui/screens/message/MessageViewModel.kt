package com.haberturm.hitchhikingapp.ui.screens.message

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.screens.profile.ProfileEvent
import com.haberturm.hitchhikingapp.ui.screens.profile.ProfileRoute
import com.haberturm.hitchhikingapp.ui.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    val routeNavigator: RouteNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), RouteNavigator by routeNavigator {

    var userMode: Constants.UserMode? = null
    private set


    init {
        when(getMode(savedStateHandle)){
            Constants.NavArgConst.DRIVER.arg -> {
                userMode = Constants.UserMode.Driver

            }
            Constants.NavArgConst.COMPANION.arg -> {
                userMode =  Constants.UserMode.Companion
            }
            else -> Unit
        }
    }

    fun onEvent(event: MessageEvent){
        when(event){
            is MessageEvent.NavigateTo -> {
                routeNavigator.navigateToRoute(event.route)
            }
        }
    }

    private fun getMode(savedStateHandle: SavedStateHandle): String {
        return ProfileRoute.getArgFrom(savedStateHandle)
    }
}