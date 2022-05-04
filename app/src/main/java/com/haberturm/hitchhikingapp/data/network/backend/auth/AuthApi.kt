package com.haberturm.hitchhikingapp.data.network.backend.auth

import com.haberturm.hitchhikingapp.data.network.AllApi
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @Headers("Content-Type: application/json")
    @POST(AllApi.SIGNUP)
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): String
}