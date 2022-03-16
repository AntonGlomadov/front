package com.haberturm.hitchhikingapp.data.network.pojo

import com.google.gson.annotations.SerializedName
import com.haberturm.hitchhikingapp.data.network.pojo.Northeast
import com.haberturm.hitchhikingapp.data.network.pojo.Southwest


data class Viewport (

    @SerializedName("northeast" ) var northeast : Northeast? = Northeast(),
    @SerializedName("southwest" ) var southwest : Southwest? = Southwest()

)