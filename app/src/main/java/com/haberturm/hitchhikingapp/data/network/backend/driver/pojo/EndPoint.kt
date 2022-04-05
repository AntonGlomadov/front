package com.haberturm.hitchhikingapp.data.network.backend.driver.pojo

import com.google.gson.annotations.SerializedName


data class EndPoint (

  @SerializedName("lat" ) var lat : Double? = null,
  @SerializedName("lng" ) var lng : Double? = null

)