package com.haberturm.hitchhikingapp.ui.util

import com.haberturm.hitchhikingapp.data.network.pojo.GeocodeLocationResponse
import com.haberturm.hitchhikingapp.data.network.pojo.Location
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.ui.model.GeocodeUiModel

object Util {
    fun GeocodeLocationResponse.toUiModel(): GeocodeUiModel{
        return GeocodeUiModel(
            formattedAddress = results[0].formattedAddress,
            location = fromLocationToLatLng(results[0].geometry.location),
        )
    }

    private fun fromLocationToLatLng(location: Location): LatLng{
        val lat: Double = location.lat
        val lng = location.lng
        return  LatLng(lat, lng)

    }
}

