package com.haberturm.hitchhikingapp.data.repositories.auth

import com.haberturm.hitchhikingapp.ui.util.Util

class AuthRepositoryImpl : AuthRepository {
    override fun checkNumberInDB(number: String): Boolean {
        return false //TODO add proper check when server will be ready
    }

    override fun checkPasswordInDB(password: String): Util.TextFieldState{
        if(password != "1"){
            return Util.TextFieldState.Failure("Неверный пароль!") //TODO add proper check when server will be ready
        }else{
            return Util.TextFieldState.Success
        }
    }

    override fun signUp(
        phoneNumber: String,
        name: String,
        password: String,
        birth: String,
        email: String
    ) {
        //TODO implement
    }
}