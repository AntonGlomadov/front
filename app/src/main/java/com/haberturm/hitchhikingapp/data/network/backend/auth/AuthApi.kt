package com.haberturm.hitchhikingapp.data.network.backend.auth

import com.haberturm.hitchhikingapp.data.network.AllApi
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.AccessToken
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.CheckRequest
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.SignUpRequest
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.UpdateInfoRequest
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @Headers("Content-Type: application/json")
    @POST(AllApi.SIGNUP)
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Response<Unit>

    @FormUrlEncoded
    @POST(AllApi.GET_TOKEN)
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "password",
        @Field("username") number: String,
        @Field("password") password: String,
        @Field("client_id") clientId: String = "companion"
    ) : Response<AccessToken>

    @Headers("Content-Type: application/json")
    @POST(AllApi.CHECK)
    suspend fun checkNumber(
        @Body number: CheckRequest
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST(AllApi.UPDT )
    suspend fun updateDriverInfo(
        //@Header("Authorization") jwtToken: AccessToken,
        @Body infoForUpdate: UpdateInfoRequest
    ): Response<Unit>

}