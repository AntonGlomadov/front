package com.haberturm.hitchhikingapp.data.network.pojo.geocode

import com.google.gson.annotations.SerializedName


data class GeocodeLocationResponse (

    @SerializedName("results" ) var results : ArrayList<Result> = arrayListOf(),
    @SerializedName("status"  ) var status  : String?            = null

)