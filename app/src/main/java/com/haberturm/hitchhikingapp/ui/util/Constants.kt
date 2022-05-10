package com.haberturm.hitchhikingapp.ui.util

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
}