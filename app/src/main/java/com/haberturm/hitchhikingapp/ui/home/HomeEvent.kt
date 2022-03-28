package com.haberturm.hitchhikingapp.ui.home

import android.content.Context
import com.google.android.gms.maps.model.LatLng

sealed class HomeEvent {
    data class IsMapReady(val isLocationReady: Boolean, val isMapReady: Boolean) : HomeEvent()
    object MapReady : HomeEvent()
    data class ObserveMovingMarkerLocation(val location: LatLng) : HomeEvent()
    data class OnMyLocationClicked(val context: Context) : HomeEvent()
    data class NavigateToSearchDirection(
        val startLocation: LatLng,
        val endLocation: LatLng
    ) : HomeEvent()

    data class GetGeocodeLocation(val address: String) : HomeEvent()
    object RelocateMarker : HomeEvent()
    object PlaceMarker : HomeEvent()
    data class MarkerPlaced(val keyOfMarker: String) : HomeEvent()
    data class MakeMarkerMovable(val keyOfMarker: String) : HomeEvent()
    data class ChangeCurrentMarkerRes(val res: Int) : HomeEvent()
    data class ColorModeChanged(val colorMode: Boolean) : HomeEvent() // true - dark, false - light
}

const val A_MARKER_KEY = "A_MARKER"
const val B_MARKER_KEY = "B_MARKER"

sealed class PermissionStatus {
    object PermissionNotProcessed : PermissionStatus()
    object PermissionAccepted : PermissionStatus()
    object PermissionDenied : PermissionStatus()
    object PermissionPermanentlyDenied : PermissionStatus()
}

