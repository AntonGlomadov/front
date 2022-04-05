package com.haberturm.hitchhikingapp.data.network.backend.driver.pojo

import com.google.gson.annotations.SerializedName
import com.haberturm.hitchhikingapp.data.network.backend.driver.pojo.Calories


data class CreateDriveRequestData (

    @SerializedName("id"       ) var id       : String?   = null,
    @SerializedName("calories" ) var calories : Calories? = Calories(),
    @SerializedName("status"   ) var status   : String?   = null

)