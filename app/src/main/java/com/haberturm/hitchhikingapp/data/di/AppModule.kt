package com.haberturm.hitchhikingapp.data.di

import android.app.Application
import com.haberturm.hitchhikingapp.UserDatabase
import com.haberturm.hitchhikingapp.data.database.UserDataSource
import com.haberturm.hitchhikingapp.data.database.UserDataSourceImpl
import com.haberturm.hitchhikingapp.data.repositories.HomeRepository
import com.haberturm.hitchhikingapp.data.repositories.HomeRepositoryImpl
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver{
        return AndroidSqliteDriver(
            schema = UserDatabase.Schema,
            context = app,
            name = "user.db"
        )
    }

    @Provides
    @Singleton
    fun providePersonDataSource(driver: SqlDriver): UserDataSource{
        return UserDataSourceImpl(UserDatabase(driver))
    }

    @Provides
    @Singleton
    fun provideHomeRepository(db : UserDataSource): HomeRepository {
        return HomeRepositoryImpl(db)
    }
}