package com.haberturm.hitchhikingapp.data.network.pojo

import com.google.gson.annotations.SerializedName


data class Geometry (

  @SerializedName("bounds"        ) var bounds       : Bounds?   = Bounds(),
  @SerializedName("location"      ) var location     : Location,
  @SerializedName("location_type" ) var locationType : String?   = null,
  @SerializedName("viewport"      ) var viewport     : Viewport? = Viewport()

)