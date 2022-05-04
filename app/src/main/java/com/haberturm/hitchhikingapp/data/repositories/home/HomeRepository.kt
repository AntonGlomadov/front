package com.haberturm.hitchhikingapp.data.repositories.home

import android.content.Context
import com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.request.CompanionFindRequestData
import com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.response.CompanionFindResponseData
import com.haberturm.hitchhikingapp.data.network.backend.driver.pojo.DriveCreateRequestData
import com.haberturm.hitchhikingapp.data.network.googleApi.pojo.directions.Direction
import com.haberturm.hitchhikingapp.data.network.googleApi.pojo.geocode.GeocodeLocationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import user.userdb.UserEntity

interface HomeRepository {
    suspend fun getUserLocationWithApi(context: Context, coroutineScope: CoroutineScope)
    fun getUserLocation(): Flow<UserEntity>
    suspend fun insertUser(id: Long?, latitude: Double, longitude: Double)
    val homeRepositoryEvent: Flow<HomeRepositoryEvent>
    fun getGeocodeLocation(address: String): Flow<GeocodeLocationResponse>
    fun getDirection(destination: String, origin: String): Flow<Direction>
    fun postCompanionFind(data: CompanionFindRequestData): Flow<List<CompanionFindResponseData>>
    fun postCreateDrive(data: DriveCreateRequestData): Flow<String>
    fun checkIfDriverExist(phoneNumber: String): Boolean
    fun sendAdditionalInfo(
        phoneNumber: String,
        carNumber: String,
        carInfo: String,
        carColor: String
    )
}