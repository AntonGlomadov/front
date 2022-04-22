package com.haberturm.hitchhikingapp.data.repositories.auth

import com.haberturm.hitchhikingapp.ui.util.Util

interface AuthRepository {
    fun checkNumberInDB(number:String): Boolean
    fun checkPasswordInDB(password:String): Util.TextFieldState
    fun signUp(
        phoneNumber: String,
        name: String,
        password: String,
        birth: String,
        email: String
    )
}