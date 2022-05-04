package com.haberturm.hitchhikingapp.data.network.backend.auth.pojo

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    val name: String,
    val surname: String,
    @SerializedName("e-mail") val email:String,
    val password: String,
    @SerializedName("birthDate") val birth: String,
    @SerializedName("phone") val phoneNumber: String
)
