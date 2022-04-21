package com.haberturm.hitchhikingapp.data.repositories.auth

import com.haberturm.hitchhikingapp.ui.util.Util

interface AuthRepository {
    fun checkNumberInDB(number:String): Boolean
    fun checkPasswordInDB(password:String): Util.TextFieldState
}