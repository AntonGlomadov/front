package com.haberturm.hitchhikingapp.ui.auth.password


sealed class PasswordEvent{
    data class UpdatePassword(val password:String) : PasswordEvent()
    data class OnPasswordFieldFocused(val focusState: Boolean) : PasswordEvent()
    object EnterPassword : PasswordEvent()
    data class OnPasswordVisibilityChange(val visibilityState: Boolean) : PasswordEvent()
}
