package com.haberturm.hitchhikingapp.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.haberturm.hitchhikingapp.data.database.UserDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import user.userdb.UserEntity

class HomeRepositoryImpl(
    private val userDataSource: UserDataSource
) : HomeRepository {


    @SuppressLint("MissingPermission")
    override suspend fun getUserLocationWithApi(context: Context, coroutineScope: CoroutineScope) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationResult = fusedLocationProviderClient.lastLocation

        locationResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val lastKnownLocation = task.result
                coroutineScope.launch {
                    insertUser(0,lastKnownLocation.latitude, lastKnownLocation.longitude)
                }
            } else {
              //TODO handle err
            }

        }
    }

    //TODO mb make following function private
    override suspend fun getUserLocation(id: Long): UserEntity? {
        return userDataSource.getUserLocation(id)
    }

    override suspend fun insertUser(id: Long?, latitude: Double, longitude: Double) {
        userDataSource.insertUser(id, latitude, longitude)
    }
}