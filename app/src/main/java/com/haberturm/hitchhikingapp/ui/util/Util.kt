package com.haberturm.hitchhikingapp.ui.util

import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.Location
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode.ReverseGeocodeResponse
import com.haberturm.hitchhikingapp.ui.model.GeocodeUiModel

object Util {
    fun GeocodeLocationResponse.toUiModel(): GeocodeUiModel{
        return GeocodeUiModel(
            formattedAddress = results[0].formattedAddress,
            location = LatLng(
                results[0].geometry.location.lat,
                results[0].geometry.location.lng),
        )
    }


    fun ReverseGeocodeResponse.toUiModel(): GeocodeUiModel{
        return GeocodeUiModel(
            formattedAddress = results[0].formattedAddress,
            location = LatLng(
                results[0].geometry.location.lat,
                results[0].geometry.location.lng),
        )
    }

}

