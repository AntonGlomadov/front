package com.haberturm.hitchhikingapp.data.network.backend.driver.pojo

import com.google.gson.annotations.SerializedName


data class EndPoint (

  @SerializedName("lat" ) var lat : Int? = null,
  @SerializedName("lng" ) var lng : Int? = null

)