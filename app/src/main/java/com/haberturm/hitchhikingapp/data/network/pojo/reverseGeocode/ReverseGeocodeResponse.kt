package com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode

import com.google.gson.annotations.SerializedName


data class ReverseGeocodeResponse (

  @SerializedName("plus_code" ) var plusCode : PlusCode?          = PlusCode(),
  @SerializedName("results"   ) var results  : ArrayList<Results> = arrayListOf(),
  @SerializedName("status"    ) var status   : String?            = null

)