package com.haberturm.hitchhikingapp.data.network.backend.companion

import com.haberturm.hitchhikingapp.data.network.AllApi
import com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.request.CompanionFindRequestData
import com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.response.CompanionFindResponseData
import retrofit2.http.*

interface CompanionApi {
    @Headers("Content-Type: application/json")
    @POST(AllApi.COMPANION_FIND)
    suspend fun postCompanionFind(
        @Body companionFindRequestData: CompanionFindRequestData
    ): List<CompanionFindResponseData>
}