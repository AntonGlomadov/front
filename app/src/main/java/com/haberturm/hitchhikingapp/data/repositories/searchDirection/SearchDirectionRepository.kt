package com.haberturm.hitchhikingapp.data.repositories.searchDirection

import com.haberturm.hitchhikingapp.data.network.googleApi.pojo.reverseGeocode.ReverseGeocodeResponse
import kotlinx.coroutines.flow.Flow

interface SearchDirectionRepository {
    fun getReverseGeocodeLocation(latlng: String) : Flow<ReverseGeocodeResponse>
}