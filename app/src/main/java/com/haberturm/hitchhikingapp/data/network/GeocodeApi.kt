package com.haberturm.hitchhikingapp.data.network

import com.haberturm.hitchhikingapp.data.network.pojo.GeocodeLocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeApi {
    @GET(AllApi.SEARCH_LOCATION)
    suspend fun getGeocodeLocation(
        @Query("address") address:String,
        @Query("key") key: String = "AIzaSyBVJFodYvEJSjlZVJDCGVNgucyV3vOLpbA"
    ): GeocodeLocationResponse
}