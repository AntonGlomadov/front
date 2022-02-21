package com.haberturm.hitchhikingapp.data.database

import com.haberturm.hitchhikingapp.UserDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import user.userdb.UserEntity
import user.userdb.UserEntityQueries

class UserDataSourceImpl(
    db: UserDatabase
) : UserDataSource {
    private val queries: UserEntityQueries = db.userEntityQueries
    override fun getUserLocation(): Flow<UserEntity> {
        return queries.getUserLocation().asFlow().mapToOne()

    }

    override suspend fun insertUser(id: Long?, latitude: Double, longitude: Double) {
        withContext(Dispatchers.IO) {
            queries.insertUser(id, latitude, longitude)
        }
    }
}