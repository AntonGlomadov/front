package com.haberturm.hitchhikingapp.data.repositories.auth

import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.AccessToken
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.SignUpRequest
import com.haberturm.hitchhikingapp.ui.util.Util
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AuthRepository {
    fun checkNumberInDB(number:String): Flow<Response<Unit>>
    fun checkPasswordInDB(number: String, password:String): Flow<Response<AccessToken>>
    fun signUp(
        signUpRequest: SignUpRequest
    ) : Flow<Response<Unit>>
}