package com.haberturm.hitchhikingapp.data.network.backend.auth.pojo

import com.google.gson.annotations.SerializedName


data class UpdateInfoRequest (
    @SerializedName("phone"     ) var phone     : String? = null,
    @SerializedName("carNumber" ) var carNumber : String? = null,
    @SerializedName("carInfo"   ) var carInfo   : String? = null

)