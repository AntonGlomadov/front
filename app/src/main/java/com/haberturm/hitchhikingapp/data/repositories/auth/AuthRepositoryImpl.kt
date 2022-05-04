package com.haberturm.hitchhikingapp.data.repositories.auth

import com.haberturm.hitchhikingapp.data.network.backend.auth.AuthRetrofit
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.SignUpRequest
import com.haberturm.hitchhikingapp.data.network.backend.companion.CompanionRetrofit
import com.haberturm.hitchhikingapp.data.network.backend.driver.DriverRetrofit
import com.haberturm.hitchhikingapp.ui.util.Util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {
    override fun checkNumberInDB(number: String): Boolean {
        return false //TODO add proper check when server will be ready
    }

    override fun checkPasswordInDB(password: String): Util.TextFieldState {
        if (password != "1") {
            return Util.TextFieldState.Failure("Неверный пароль!") //TODO add proper check when server will be ready
        } else {
            return Util.TextFieldState.Success
        }
    }

    override fun signUp(signUpRequest: SignUpRequest): Flow<String> {
        return flow { AuthRetrofit.authRetrofit.signUp(signUpRequest) }
    }
}