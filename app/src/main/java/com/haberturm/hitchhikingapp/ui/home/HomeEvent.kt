package com.haberturm.hitchhikingapp.ui.home

import com.google.android.gms.maps.model.LatLng

sealed class HomeEvent {
    data class IsMapReady(val isLocationReady: Boolean, val isMapReady: Boolean) : HomeEvent()
    object MapReady : HomeEvent()
    data class MarkerLocationChanged(val location:LatLng): HomeEvent()
}
