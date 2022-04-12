package com.haberturm.hitchhikingapp.ui.auth

sealed class AuthEvent{
    data class OnPhoneFieldFocused(val focusState: Boolean) : AuthEvent()
    data class UpdateNumber(val number: String) : AuthEvent()

}