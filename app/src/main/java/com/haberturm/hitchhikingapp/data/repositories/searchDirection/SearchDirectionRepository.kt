package com.haberturm.hitchhikingapp.data.repositories.searchDirection

import android.content.Context
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
import com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode.ReverseGeocodeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import user.userdb.UserEntity

interface SearchDirectionRepository {
    fun getReverseGeocodeLocation(latlng: String) : Flow<ReverseGeocodeResponse>
}