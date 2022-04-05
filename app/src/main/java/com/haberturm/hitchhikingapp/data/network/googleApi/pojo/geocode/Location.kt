package com.haberturm.hitchhikingapp.data.network.googleApi.pojo.geocode

import com.google.gson.annotations.SerializedName


data class Location (

  @SerializedName("lat" ) var lat : Double,
  @SerializedName("lng" ) var lng : Double

)