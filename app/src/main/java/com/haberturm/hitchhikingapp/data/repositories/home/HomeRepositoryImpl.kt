package com.haberturm.hitchhikingapp.data.repositories.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.haberturm.hitchhikingapp.data.network.googleApi.pojo.geocode.GeocodeLocationResponse
import com.google.android.gms.location.LocationServices
import com.haberturm.hitchhikingapp.data.database.UserDataSource
import com.haberturm.hitchhikingapp.data.network.backend.auth.AuthRetrofit
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.AccessToken
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.CheckRequest
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.UpdateInfoRequest
import com.haberturm.hitchhikingapp.data.network.backend.companion.CompanionRetrofit
import com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.request.CompanionFindRequestData
import com.haberturm.hitchhikingapp.data.network.backend.companion.pojo.companion.response.CompanionFindResponseData
import com.haberturm.hitchhikingapp.data.network.backend.driver.DriverRetrofit
import com.haberturm.hitchhikingapp.data.network.backend.driver.pojo.DriveCreateRequestData
import com.haberturm.hitchhikingapp.data.network.googleApi.GoogleRetrofit
import com.haberturm.hitchhikingapp.data.network.googleApi.pojo.directions.Direction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import user.userdb.UserEntity

class HomeRepositoryImpl(
    private val userDataSource: UserDataSource
) : HomeRepository {

    private val _homeRepositoryEvent = Channel<HomeRepositoryEvent>()
    override val homeRepositoryEvent = _homeRepositoryEvent.receiveAsFlow()

    override fun getGeocodeLocation(address: String): Flow<GeocodeLocationResponse> = flow {
        val geocodeLocationResponse = GoogleRetrofit.googleApi.getGeocodeLocation(address = address)
        emit(geocodeLocationResponse)
    }.flowOn(Dispatchers.IO)

    override fun getDirection(destination: String, origin: String): Flow<Direction> = flow {
        val directionResponse = GoogleRetrofit.googleApi.getDirections(destination, origin)
        Log.i("API-RESP", "${directionResponse}")
        emit(directionResponse)
    }.flowOn(Dispatchers.IO)

// this how it will be
//    override fun postCompanionFind(data: CompanionFindRequest): Flow<List<CompanionFindResponse>> =
//        flow<List<CompanionFindResponse>> {
//            val companionFindResponse = BackendRetrofit.backendRetrofit.postCompanionFind(data)
//        }.flowOn(Dispatchers.IO)

    //demo
    override fun postCompanionFind(data: CompanionFindRequestData): Flow<List<CompanionFindResponseData>> {
        Log.i("POST-API-RESP", "in repo")
        return flow { CompanionRetrofit.companionRetrofit.postCompanionFind(data) }

    }

    override fun postCreateDrive(data: DriveCreateRequestData): Flow<String> {
        Log.i("POST-API-RESP", "in repo")
        return flow { DriverRetrofit.driverRetrofit.postCreateDrive(data) }
    }

    override fun checkIfDriverExist(phoneNumber: String): Flow<Response<Unit>> = flow {
        val r = AuthRetrofit.authRetrofit.checkNumber(
            CheckRequest(phoneNumber)
        )
        emit(r)
    }.flowOn(Dispatchers.IO)

    override fun sendAdditionalInfo(
        phoneNumber: String,
        carNumber: String,
        carInfo: String,
        carColor: String,
    ) : Flow<Response<Unit>> = flow {
        val r = AuthRetrofit.authRetrofit.updateDriverInfo(
            UpdateInfoRequest(
                phone = phoneNumber,
                carNumber = carNumber,
                carInfo = "$carInfo; Цвет: $carColor"
            )
        )
        emit(r)
    }.flowOn(Dispatchers.IO)

//    @SuppressLint("MissingPermission")
//    override fun getUserLocationWithApi(context: Context, coroutineScope: CoroutineScope): Boolean {
//        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//        val locationResult = fusedLocationProviderClient.lastLocation
//        coroutineScope.launch {_homeRepositoryEvent.send(HomeRepositoryEvent.UserLocationStatus(isDone = false))  }
//        locationResult.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val lastKnownLocation =
//                    task.result //TODO handle case when lastKnownLocation is null
//                coroutineScope.launch {
//                    async {
//                        insertUser(0, lastKnownLocation.latitude, lastKnownLocation.longitude)
//                    }.await()
//                }
//            } else {
//
//                Log.i("EXCEPTION-inGetUserLocation_rep", "${task.exception}")
//                //TODO handle err
//            }
//        }
//        return true
//    }

//    //TODO mb make following function private
//    override fun getUserLocation(): Flow<UserEntity> {
//        return userDataSource.getUserLocation()
//    }

    override suspend fun insertUser(number: String, password: String) {
        userDataSource.insertUser(number, password)
    }

    override suspend fun getUserData(): UserEntity? {
        return userDataSource.getUserData()
    }
}