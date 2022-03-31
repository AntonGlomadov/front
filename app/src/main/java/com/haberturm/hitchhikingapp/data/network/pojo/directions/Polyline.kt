package com.haberturm.hitchhikingapp.data.network.pojo.directions

import com.google.gson.annotations.SerializedName


data class Polyline (

  @SerializedName("points" ) var points : String

)