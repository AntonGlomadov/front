package com.haberturm.hitchhikingapp.data.repositories.auth

import com.haberturm.hitchhikingapp.ui.auth.password.PasswordState

interface AuthRepository {
    fun checkNumberInDB(number:String): Boolean
    fun checkPasswordInDB(password:String): PasswordState
}