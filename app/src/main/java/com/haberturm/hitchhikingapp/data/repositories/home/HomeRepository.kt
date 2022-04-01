package com.haberturm.hitchhikingapp.data.repositories.home

import android.content.Context
import com.haberturm.hitchhikingapp.data.network.pojo.directions.Direction
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
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
}