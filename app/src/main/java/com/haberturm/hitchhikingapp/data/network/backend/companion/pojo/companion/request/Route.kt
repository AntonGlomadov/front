package com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.request

import com.google.gson.annotations.SerializedName


data class Route (

    @SerializedName("startPoint" ) var startPoint : StartPoint? = StartPoint(),
    @SerializedName("endPoint"   ) var endPoint   : EndPoint?   = EndPoint()

)