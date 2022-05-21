package com.haberturm.hitchhikingapp.ui.screens.message

import com.haberturm.hitchhikingapp.ui.screens.profile.ProfileEvent

sealed class MessageEvent{
    data class NavigateTo(val route: String): MessageEvent()
}
