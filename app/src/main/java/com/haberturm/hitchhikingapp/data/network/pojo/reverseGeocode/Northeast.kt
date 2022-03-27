package com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode

import com.google.gson.annotations.SerializedName


data class Northeast (

  @SerializedName("lat" ) var lat : Double? = null,
  @SerializedName("lng" ) var lng : Double? = null

)