package com.haberturm.hitchhikingapp.data.repositories.auth

import android.util.Log
import com.haberturm.hitchhikingapp.data.network.backend.auth.AuthRetrofit
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.AccessToken
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.SignUpRequest
import com.haberturm.hitchhikingapp.data.network.backend.companion.CompanionRetrofit
import com.haberturm.hitchhikingapp.data.network.backend.driver.DriverRetrofit
import com.haberturm.hitchhikingapp.ui.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class AuthRepositoryImpl : AuthRepository {
    override fun checkNumberInDB(number: String): Boolean {
        return false //TODO add proper check when server will be ready
    }

    override fun checkPasswordInDB(
        number: String,
        password: String
    ): Flow<Response<AccessToken>> = flow {
            val r = AuthRetrofit.authRetrofit.getAccessToken(
                number = number,
                password = password
            )
            emit(r)
        }.flowOn(Dispatchers.IO)


    override fun signUp(signUpRequest: SignUpRequest): Flow<Response<Unit>> = flow {
        val r = AuthRetrofit.authRetrofit.signUp(signUpRequest)
        emit(r)
    }.flowOn(Dispatchers.IO)
}