package com.haberturm.hitchhikingapp.data.network.pojo

import com.google.gson.annotations.SerializedName
import com.haberturm.hitchhikingapp.data.network.pojo.Result


data class GeocodeLocationResponse (

  @SerializedName("results" ) var results : ArrayList<Result> = arrayListOf(),
  @SerializedName("status"  ) var status  : String?            = null

)