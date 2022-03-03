package com.haberturm.hitchhikingapp.ui.home.map

import android.location.Geocoder
import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.home.HomeViewModel


@Composable
fun GoogleMapView(
    latitude: Double,
    longitude: Double,
    locationPermissionGranted: Boolean,
    modifier: Modifier,
    onMapLoaded: () -> Unit,
    viewModel: HomeViewModel,
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
        LocationMarker(cameraPositionState, viewModel)

    }
    TextField(value = "Поиск города", onValueChange = {/*TODO make request*/})
    LocationPicker(cameraPositionState,viewModel)
}

@Composable
fun LocationPicker(cameraPositionState: CameraPositionState, viewModel: HomeViewModel) {
    if(!cameraPositionState.isMoving){
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Поехали! ")
        }
        val loc = viewModel.markerLocation.collectAsState()
        Text(text = loc.value.toString())
    }
}


@Composable
fun LocationMarker(cameraPositionState: CameraPositionState, viewModel: HomeViewModel) {
    Marker(position = cameraPositionState.position.target)
    viewModel.onEvent(HomeEvent.MarkerLocationChanged(cameraPositionState.position.target))
}
