package com.haberturm.hitchhikingapp.ui.home.map

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@Composable
fun GoogleMapView(
    latitude: Double,
    longitude: Double,
    locationPermissionGranted: Boolean,
    modifier: Modifier,
    onMapLoaded: () -> Unit,
) {
    val location = LatLng(latitude, longitude)
    Log.i("LOCATION", "${location}")
    // Observing and controlling the camera's state can be done with a CameraPositionState
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }
    var mapProperties = if (locationPermissionGranted) {
        remember {
            mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true))
        }
    } else {
        remember {
            mutableStateOf(MapProperties(mapType = MapType.NORMAL))
        }
    }
    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false, myLocationButtonEnabled = true)) }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties.value,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        googleMapOptionsFactory = {
            GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(location, 16f))
        },
        onPOIClick = {
            Log.d("TAG", "POI clicked: ${it.name}")
        }
    ) {
        cameraPositionState.move(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(location,16f)) //TODO check all zoom bugs (mb save zoom value)
    }
}