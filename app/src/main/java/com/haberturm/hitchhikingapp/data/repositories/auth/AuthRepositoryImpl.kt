package com.haberturm.hitchhikingapp.data.repositories.auth

import com.haberturm.hitchhikingapp.ui.auth.password.PasswordState

class AuthRepositoryImpl : AuthRepository {
    override fun checkNumberInDB(number: String): Boolean {
        return true //TODO add proper check when server will be ready
    }

    override fun checkPasswordInDB(password: String): PasswordState {
        if(password != "1"){
            return PasswordState.Failure("Неверный пароль!") //TODO add proper check when server will be ready
        }else{
            return PasswordState.Success
        }
    }
}