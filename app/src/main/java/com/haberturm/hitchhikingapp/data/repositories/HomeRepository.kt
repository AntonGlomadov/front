package com.haberturm.hitchhikingapp.data.repositories

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import user.userdb.UserEntity

interface HomeRepository {
    suspend fun getUserLocationWithApi(context: Context, coroutineScope: CoroutineScope)
    suspend fun getUserLocation(): Flow<UserEntity>
    suspend fun insertUser(id: Long?, latitude: Double, longitude: Double)
    val homeRepositoryEvent: Flow<HomeRepositoryEvent>

}