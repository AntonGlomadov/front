package com.haberturm.hitchhikingapp.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
import com.google.android.gms.location.LocationServices
import com.haberturm.hitchhikingapp.data.database.UserDataSource
import com.haberturm.hitchhikingapp.data.network.Retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import user.userdb.UserEntity

class HomeRepositoryImpl(
    private val userDataSource: UserDataSource
) : HomeRepository {


    private val _homeRepositoryEvent =  Channel<HomeRepositoryEvent>()
    override val homeRepositoryEvent = _homeRepositoryEvent.receiveAsFlow()

    override fun getGeocodeLocation(address:String): Flow<GeocodeLocationResponse> = flow{
        val p = Retrofit.retrofit.getGeocodeLocation(address)
        Log.i("TESTAPI", p.toString())
        emit(p)
    }.flowOn(Dispatchers.IO)


    @SuppressLint("MissingPermission")
    override suspend fun getUserLocationWithApi(context: Context, coroutineScope: CoroutineScope) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationResult = fusedLocationProviderClient.lastLocation
        _homeRepositoryEvent.send(HomeRepositoryEvent.UserLocationStatus(isDone = false))
        locationResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val lastKnownLocation = task.result
                coroutineScope.launch {
                    async {
                        insertUser(0,lastKnownLocation.latitude, lastKnownLocation.longitude)
                    }.await()

                }
            } else {
              //TODO handle err
            }

        }
    }

    //TODO mb make following function private
    override fun getUserLocation(): Flow<UserEntity> {
        return userDataSource.getUserLocation()
    }

    override suspend fun insertUser(id: Long?, latitude: Double, longitude: Double) {
        userDataSource.insertUser(id, latitude, longitude)
        _homeRepositoryEvent.send(HomeRepositoryEvent.UserLocationStatus(isDone = true))
    }
}