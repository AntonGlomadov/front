package com.haberturm.hitchhikingapp.data.database

import user.userdb.UserEntity

interface UserDataSource {
    suspend fun getUserLocation(id: Long): UserEntity?
    suspend fun insertUser(id: Long?, latitude: Double, longitude: Double)

}