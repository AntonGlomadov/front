package com.haberturm.hitchhikingapp.data.database

import kotlinx.coroutines.flow.Flow
import user.userdb.UserEntity

interface UserDataSource {
    suspend fun getUserData(): UserEntity?
    suspend fun insertUser(number: String, password: String)

}