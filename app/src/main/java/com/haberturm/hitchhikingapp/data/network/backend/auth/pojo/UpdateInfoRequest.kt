package com.haberturm.hitchhikingapp.data.network.backend.auth.pojo

import com.google.gson.annotations.SerializedName


data class UpdateInfoRequest (
    var phone     : String,
    var carNumber : String,
    var carInfo   : String
)