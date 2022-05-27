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
    override suspend fun getUserData(): UserEntity? {
        return queries.getUserData().executeAsOneOrNull()
    }

    override suspend fun insertUser(number: String, password:String) {
        withContext(Dispatchers.IO) {
            queries.insertUser(number, password)
        }
    }
}