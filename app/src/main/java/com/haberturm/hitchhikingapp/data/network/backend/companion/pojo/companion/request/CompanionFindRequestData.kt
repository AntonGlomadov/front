package com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.request

import com.google.gson.annotations.SerializedName


data class CompanionFindRequestData (

    @SerializedName("route"   ) var route   : Route? = Route(),
    @SerializedName("time"    ) var time    : Int?   = null,
    @SerializedName("percent" ) var percent : Int?   = null

)