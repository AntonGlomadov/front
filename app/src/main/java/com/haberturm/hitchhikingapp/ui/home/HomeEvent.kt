package com.haberturm.hitchhikingapp.ui.home

import android.content.Context
import com.google.android.gms.maps.model.LatLng

sealed class HomeEvent {
    data class IsMapReady(val isLocationReady: Boolean, val isMapReady: Boolean) : HomeEvent()
    object MapReady : HomeEvent()
    data class MarkerLocationChanged(val location: LatLng) : HomeEvent()
    data class PermissionEvent(val status: PermissionStatus) : HomeEvent()
    data class OnMyLocationClicked(val context: Context) : HomeEvent()
}

sealed class PermissionStatus {
    object PermissionNotProcessed : PermissionStatus()
    object PermissionAccepted : PermissionStatus()
    object PermissionDenied : PermissionStatus()
    object PermissionPermanentlyDenied : PermissionStatus()
}