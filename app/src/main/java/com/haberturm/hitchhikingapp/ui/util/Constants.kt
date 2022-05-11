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
}