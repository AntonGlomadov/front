package com.haberturm.hitchhikingapp.data.repositories.auth

import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.SignUpRequest
import com.haberturm.hitchhikingapp.ui.util.Util
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun checkNumberInDB(number:String): Boolean
    fun checkPasswordInDB(password:String): Util.TextFieldState
    fun signUp(
        signUpRequest: SignUpRequest
    ) : Flow<String>
}