package com.haberturm.hitchhikingapp.data.network.googleApi.pojo.reverseGeocode

import com.google.gson.annotations.SerializedName


data class Viewport (

    @SerializedName("northeast" ) var northeast : Northeast? = Northeast(),
    @SerializedName("southwest" ) var southwest : Southwest? = Southwest()

)