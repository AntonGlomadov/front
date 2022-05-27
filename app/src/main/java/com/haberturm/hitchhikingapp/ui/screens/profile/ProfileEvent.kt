package com.haberturm.hitchhikingapp.ui.screens.profile

import com.haberturm.hitchhikingapp.ui.screens.home.HomeEvent

sealed class ProfileEvent{
    object UpdateDropDownState : ProfileEvent()
    object UpdateModeSwitchState: ProfileEvent()
    data class NavigateTo(val route: String): ProfileEvent()

    //additional dialog
    data class UpdateCarNumberTextValue(val textValue:String) : ProfileEvent()
    data class UpdateCarInfoTextValue(val textValue:String) : ProfileEvent()
    data class UpdateCarColorTextValue(val textValue:String) : ProfileEvent()
    object SendAdditionalInfo : ProfileEvent()
    object OnDismissAdditionalInfo : ProfileEvent()
}
