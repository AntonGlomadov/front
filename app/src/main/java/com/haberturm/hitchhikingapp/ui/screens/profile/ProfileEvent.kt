package com.haberturm.hitchhikingapp.ui.screens.profile

sealed class ProfileEvent{
    object UpdateDropDownState : ProfileEvent()
    object UpdateModeSwitchState: ProfileEvent()
    data class NavigateTo(val route: String): ProfileEvent()
}
