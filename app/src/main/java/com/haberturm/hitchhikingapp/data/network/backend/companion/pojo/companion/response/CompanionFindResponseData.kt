package com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.response

import com.google.gson.annotations.SerializedName


data class CompanionFindResponseData (

  @SerializedName("name"    ) var name    : String? = null,
  @SerializedName("time"    ) var time    : Int?    = null,
  @SerializedName("percent" ) var percent : Int?    = null

)