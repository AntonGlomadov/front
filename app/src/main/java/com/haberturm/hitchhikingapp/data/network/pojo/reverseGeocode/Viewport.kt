package com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode

import com.google.gson.annotations.SerializedName
import com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode.Northeast
import com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode.Southwest


data class Viewport (

  @SerializedName("northeast" ) var northeast : Northeast? = Northeast(),
  @SerializedName("southwest" ) var southwest : Southwest? = Southwest()

)