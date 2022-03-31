package com.haberturm.hitchhikingapp.data.network

import com.haberturm.hitchhikingapp.data.network.pojo.directions.Direction
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApi {
    @GET(AllApi.DIRECTIONS)
    suspend fun getDirections(
        @Query("destination") destination: String,
        @Query("origin") origin: String,
        @Query("key") key: String = "AIzaSyBVJFodYvEJSjlZVJDCGVNgucyV3vOLpbA"
    ): Direction
}