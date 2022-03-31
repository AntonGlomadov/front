package com.haberturm.hitchhikingapp.data.network

import com.haberturm.hitchhikingapp.data.network.pojo.directions.Direction
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
import com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode.ReverseGeocodeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleApi {
    @GET(AllApi.GEOCODE_LOCATION)
    suspend fun getGeocodeLocation(
        @Query("address") address:String,
        @Query("key") key: String = "AIzaSyBVJFodYvEJSjlZVJDCGVNgucyV3vOLpbA"
    ): GeocodeLocationResponse

    @GET(AllApi.REVERSE_GEOCODE_LOCATION)
    suspend fun getReverseGeocodeLocation(
        @Query("latlng") latlng:String,
        @Query("key") key: String = "AIzaSyBVJFodYvEJSjlZVJDCGVNgucyV3vOLpbA"
    ): ReverseGeocodeResponse

    @GET(AllApi.DIRECTIONS)
    suspend fun getDirections(
        @Query("destination") destination: String,
        @Query("origin") origin: String,
        @Query("key") key: String = "AIzaSyBVJFodYvEJSjlZVJDCGVNgucyV3vOLpbA"
    ): Direction
}