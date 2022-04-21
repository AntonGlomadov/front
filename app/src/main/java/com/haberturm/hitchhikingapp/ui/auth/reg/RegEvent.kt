package com.haberturm.hitchhikingapp.ui.auth.reg

sealed class RegEvent{
    //phone
    data class OnPhoneFieldFocused(val focusState: Boolean) : RegEvent()
    data class UpdateNumber(val number: String) : RegEvent()
    //name
    data class OnNameFieldFocused(val focusState: Boolean) : RegEvent()
    data class UpdateName(val name: String) : RegEvent()
    //password
    data class UpdatePassword(val password:String) : RegEvent()
    data class OnPasswordFieldFocused(val focusState: Boolean) : RegEvent()
    data class OnPasswordVisibilityChange(val visibilityState: Boolean) : RegEvent()
    //repeat password
    data class UpdateRepeatPassword(val password:String) : RegEvent()
    data class OnRepeatPasswordFieldFocused(val focusState: Boolean) : RegEvent()
    data class OnRepeatPasswordVisibilityChange(val visibilityState: Boolean) : RegEvent()
    //birth
    data class UpdateBirth(val birth:String) : RegEvent()
    data class OnBirthFieldFocused(val focusState: Boolean) : RegEvent()
    //email
    data class UpdateEmail(val email:String) : RegEvent()
    data class OnEmailFieldFocused(val focusState: Boolean) : RegEvent()

    object SignUp : RegEvent()
}
