package com.haberturm.hitchhikingapp.data.repositories.auth


import com.haberturm.hitchhikingapp.data.network.backend.auth.AuthRetrofit
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.AccessToken
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.CheckRequest
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.SignUpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class AuthRepositoryImpl : AuthRepository {
    override fun checkNumberInDB(number: String): Flow<Response<Unit>> = flow {
        val r = AuthRetrofit.authRetrofit.checkNumber(
            CheckRequest(number)
        )
        emit(r)
    }.flowOn(Dispatchers.IO)

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