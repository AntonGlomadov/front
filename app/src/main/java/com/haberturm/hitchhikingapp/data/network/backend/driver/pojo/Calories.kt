package com.haberturm.hitchhikingapp.data.network.backend.driver.pojo

import com.google.gson.annotations.SerializedName


data class Calories (

  @SerializedName("startPoint" ) var startPoint : StartPoint? = StartPoint(),
  @SerializedName("endPoint"   ) var endPoint   : EndPoint?   = EndPoint()

)