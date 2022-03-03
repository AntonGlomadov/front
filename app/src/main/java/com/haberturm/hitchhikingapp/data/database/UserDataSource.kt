package com.haberturm.hitchhikingapp.data.database

import kotlinx.coroutines.flow.Flow
import user.userdb.UserEntity

interface UserDataSource {
    fun getUserLocation(): Flow<UserEntity>
    suspend fun insertUser(id: Long?, latitude: Double, longitude: Double)

}