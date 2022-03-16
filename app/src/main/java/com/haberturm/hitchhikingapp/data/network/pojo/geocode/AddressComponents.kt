package com.haberturm.hitchhikingapp.data.network.pojo

import com.google.gson.annotations.SerializedName


data class AddressComponents (

  @SerializedName("long_name"  ) var longName  : String?           = null,
  @SerializedName("short_name" ) var shortName : String?           = null,
  @SerializedName("types"      ) var types     : ArrayList<String> = arrayListOf()

)