package com.haberturm.hitchhikingapp.ui.util

import com.haberturm.hitchhikingapp.BuildConfig

object Constants {
    sealed class UserMode{
        object Companion : UserMode()
        object Driver : UserMode()
        object Undefined : UserMode()
    }

    enum class NavArgConst(val arg: String){
        COMPANION("COMPANION"),
        DRIVER("DRIVER"),
        UNDEFINED("UNDEFINED")
    }
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

    const val ACTION_SHOW_PROPER_MAP_STATE = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val ACCESS_TOKEN = "ACCESS_TOKEN"
}