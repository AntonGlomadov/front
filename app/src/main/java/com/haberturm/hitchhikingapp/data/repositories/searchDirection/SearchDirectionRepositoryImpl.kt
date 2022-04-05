package com.haberturm.hitchhikingapp.data.repositories.searchDirection

import android.util.Log
import com.haberturm.hitchhikingapp.data.network.googleApi.GoogleRetrofit
import com.haberturm.hitchhikingapp.data.network.googleApi.pojo.reverseGeocode.ReverseGeocodeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchDirectionRepositoryImpl(
) : SearchDirectionRepository {

    override fun getReverseGeocodeLocation(latlng: String): Flow<ReverseGeocodeResponse> = flow{
        val p = GoogleRetrofit.googleApi.getReverseGeocodeLocation(latlng = latlng)
        Log.i("TESTAPI", p.toString())
        emit(p)
    }.flowOn(Dispatchers.IO)

}