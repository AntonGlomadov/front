package com.haberturm.hitchhikingapp.ui.screens.home

import android.content.Context
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.ui.util.Constants
import kotlinx.parcelize.Parcelize

sealed class HomeEvent {
    data class IsMapReady(val isLocationReady: Boolean, val isMapReady: Boolean) : HomeEvent()
    object MapReady : HomeEvent()
    object LocationReady : HomeEvent()
    data class ObserveMovingMarkerLocation(val location: LatLng) : HomeEvent()
    data class OnMyLocationClicked(val context: Context) : HomeEvent()
    //unused
    data class NavigateToSearchDirection(
        val startLocation: LatLng,
        val endLocation: LatLng
    ) : HomeEvent()
    //
    data class NavigateTo(val route: String): HomeEvent()

    data class GetGeocodeLocation(val address: String) : HomeEvent()
    data class RelocateMarker(val location: LatLng, val animation: Boolean) : HomeEvent()
    object PlaceMarker : HomeEvent()
    data class MarkerPlaced(val keyOfMarker: String) : HomeEvent()
    object IsNotInRadius : HomeEvent()
    data class MakeMarkerMovable(val keyOfMarker: String) : HomeEvent()
    data class ChangeCurrentMarkerRes(val res: Int) : HomeEvent()
    data class ColorModeChanged(val colorMode: Boolean) : HomeEvent() // true - dark, false - light
    data class ChangeUserMode(val mode: Constants.UserMode) : HomeEvent()

    data class ShowError(val e: Throwable): HomeEvent()
    object RecreateAfterError : HomeEvent()
    object InitUserMode: HomeEvent()

    //additional dialog
    data class UpdateCarNumberTextValue(val textValue:String) : HomeEvent()
    data class UpdateCarInfoTextValue(val textValue:String) : HomeEvent()
    data class UpdateCarColorTextValue(val textValue:String) : HomeEvent()
    object SendAdditionalInfo : HomeEvent()
    object OnDismissAdditionalInfo : HomeEvent()

    object StartRide : HomeEvent()
    object StopRide : HomeEvent()
}

const val A_MARKER_KEY = "A_MARKER"
const val B_MARKER_KEY = "B_MARKER"

sealed class PermissionStatus {
    object PermissionNotProcessed : PermissionStatus()
    object PermissionAccepted : PermissionStatus()
    object PermissionDenied : PermissionStatus()
    object PermissionPermanentlyDenied : PermissionStatus()
}


