package com.haberturm.hitchhikingapp.data.network.googleApi.pojo.directions

import com.google.gson.annotations.SerializedName


data class Direction (

    @SerializedName("geocoded_waypoints" ) var geocodedWaypoints : ArrayList<GeocodedWaypoints> = arrayListOf(),
    @SerializedName("routes"             ) var routes            : ArrayList<Routes>            = arrayListOf(),
    @SerializedName("status"             ) var status            : String?                      = null

)