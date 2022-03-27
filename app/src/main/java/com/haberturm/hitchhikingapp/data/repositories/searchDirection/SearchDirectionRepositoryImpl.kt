package com.haberturm.hitchhikingapp.data.repositories.searchDirection

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
import com.google.android.gms.location.LocationServices
import com.haberturm.hitchhikingapp.data.database.UserDataSource
import com.haberturm.hitchhikingapp.data.network.Retrofit
import com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode.ReverseGeocodeResponse
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

class SearchDirectionRepositoryImpl(
) : SearchDirectionRepository {

    override fun getReverseGeocodeLocation(latlng: String): Flow<ReverseGeocodeResponse> = flow{
        val p = Retrofit.retrofit.getReverseGeocodeLocation(latlng)
        Log.i("TESTAPI", p.toString())
        emit(p)
    }.flowOn(Dispatchers.IO)

}