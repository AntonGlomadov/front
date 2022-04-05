package com.haberturm.hitchhikingapp.data.network.googleApi.pojo.reverseGeocode

import com.google.gson.annotations.SerializedName


data class Geometry (

    @SerializedName("location"      ) var location     : Location,
    @SerializedName("location_type" ) var locationType : String?   = null,
    @SerializedName("viewport"      ) var viewport     : Viewport? = Viewport()

)