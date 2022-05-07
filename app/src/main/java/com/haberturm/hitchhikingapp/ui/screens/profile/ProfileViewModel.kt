package com.haberturm.hitchhikingapp.ui.screens.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val routeNavigator: RouteNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), RouteNavigator by routeNavigator {
    private val _dropDownState = MutableStateFlow(false)
    val dropDownState = _dropDownState.asStateFlow()

    private val _modeSwitchState = MutableStateFlow(false) // false - companion, true - driver
    val modeSwitchState = _modeSwitchState.asStateFlow()

    private val _userMode = MutableStateFlow<Constants.UserMode>(Constants.UserMode.Companion)
    val userMode = _userMode.asStateFlow()

    init {
        when(getMode(savedStateHandle)){
            Constants.NavArgConst.DRIVER.arg -> {
                _userMode.value = Constants.UserMode.Driver
                _modeSwitchState.value = true

            }
            Constants.NavArgConst.COMPANION.arg -> {
                _userMode.value = Constants.UserMode.Companion
                _modeSwitchState.value = false
            }
            else -> Unit
        }
    }

    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.UpdateDropDownState -> {
                _dropDownState.value = !dropDownState.value
            }
            is ProfileEvent.UpdateModeSwitchState -> {
                _modeSwitchState.value = !modeSwitchState.value
                _userMode.value = if(modeSwitchState.value){
                    Constants.UserMode.Driver
                }else{
                    Constants.UserMode.Companion
                }
            }
            is ProfileEvent.NavigateTo -> {
                routeNavigator.navigateToRoute(event.route)
            }
        }
    }

    private fun getMode(savedStateHandle: SavedStateHandle): String {
        return ProfileRoute.getArgFrom(savedStateHandle)
    }
}