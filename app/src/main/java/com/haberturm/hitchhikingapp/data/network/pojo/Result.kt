package com.haberturm.hitchhikingapp.data.network.pojo

import com.google.gson.annotations.SerializedName


data class Result (

  @SerializedName("address_components" ) var addressComponents : ArrayList<AddressComponents> = arrayListOf(),
  @SerializedName("formatted_address"  ) var formattedAddress  : String?                      = null,
  @SerializedName("geometry"           ) var geometry          : Geometry,
  @SerializedName("place_id"           ) var placeId           : String?                      = null,
  @SerializedName("types"              ) var types             : ArrayList<String>            = arrayListOf()

)