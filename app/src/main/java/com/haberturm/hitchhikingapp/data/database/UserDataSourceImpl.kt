package com.haberturm.hitchhikingapp.data.database

import com.haberturm.hitchhikingapp.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import user.userdb.UserEntity
import user.userdb.UserEntityQueries

class UserDataSourceImpl(
    db: UserDatabase
) : UserDataSource {
    private val queries: UserEntityQueries = db.userEntityQueries
    override suspend fun getUserLocation(id: Long): UserEntity? {
        return withContext(Dispatchers.IO) {
            queries.getUserLocation(id).executeAsOneOrNull()
        }
    }

    override suspend fun insertUser(id: Long?, latitude: Double, longitude: Double) {
        withContext(Dispatchers.IO) {
            queries.insertUser(id, latitude, longitude)
        }
    }
}