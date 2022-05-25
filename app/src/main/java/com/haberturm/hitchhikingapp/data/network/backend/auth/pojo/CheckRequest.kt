package com.haberturm.hitchhikingapp.data.network.backend.auth.pojo

import com.google.gson.annotations.SerializedName


data class CheckRequest (
    @SerializedName("phone" ) var phone : String? = null
)