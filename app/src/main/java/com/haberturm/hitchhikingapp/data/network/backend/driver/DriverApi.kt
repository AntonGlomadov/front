package com.haberturm.hitchhikingapp.data.network.backend.driver

import com.haberturm.hitchhikingapp.data.network.AllApi
import com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.request.CompanionFindRequestData
import com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.response.CompanionFindResponseData
import com.haberturm.hitchhikingapp.data.network.backend.driver.pojo.CreateDriveRequestData
import retrofit2.http.*

interface DriverApi {
    @Headers("Content-Type: application/json")
    @POST(AllApi.DRIVE_CREATE)
    suspend fun postCreateDrive(
        @Body createDriveRequestData: CreateDriveRequestData
    ): String
}