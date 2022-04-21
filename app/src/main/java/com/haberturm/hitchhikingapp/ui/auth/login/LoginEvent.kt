package com.haberturm.hitchhikingapp.ui.auth.login

sealed class LoginEvent {
    data class OnPhoneFieldFocused(val focusState: Boolean) : LoginEvent()
    data class UpdateNumber(val number: String) : LoginEvent()
    object EnterNumber : LoginEvent()
}


object PhoneErrors {
    const val LENGTH_ERR = "Неверный формат номера: проверьте длину!"
    const val PLUS_START_ERR = "Неверный формат номера: номер должен начинаться с '+'"
    const val WRONG_FORMAT_ERR = "Неверный формат номера"
}