package com.haberturm.hitchhikingapp.data.network.googleApi.pojo.directions

import com.google.gson.annotations.SerializedName


data class Polyline (

  @SerializedName("points" ) var points : String

)