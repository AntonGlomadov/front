package com.haberturm.hitchhikingapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.screens.home.UserMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val routeNavigator: RouteNavigator
) : ViewModel(), RouteNavigator by routeNavigator {
    private val _dropDownState = MutableStateFlow(false)
    val dropDownState = _dropDownState.asStateFlow()

    private val _modeSwitchState = MutableStateFlow(false) // false - companion, true - driver
    val modeSwitchState = _modeSwitchState.asStateFlow()

    private val _userMode = MutableStateFlow<UserMode>(UserMode.Companion)
    val userMode = _userMode.asStateFlow()

    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.UpdateDropDownState -> {
                _dropDownState.value = !dropDownState.value
            }
            is ProfileEvent.UpdateModeSwitchState -> {
                _modeSwitchState.value = !modeSwitchState.value
                _userMode.value = if(modeSwitchState.value){
                    UserMode.Driver
                }else{
                    UserMode.Companion
                }
            }
        }
    }

}