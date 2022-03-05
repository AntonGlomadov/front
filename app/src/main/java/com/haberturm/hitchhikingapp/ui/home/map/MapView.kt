package com.haberturm.hitchhikingapp.ui.home.map

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.haberturm.hitchhikingapp.R
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
    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false
            )
        )
    }
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

        cameraPositionState.move(
            com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                location,
                16f
            )
        ) //TODO check all zoom bugs (mb save zoom value)
        LocationMarker(cameraPositionState, viewModel)

    }

    MapHood(cameraPositionState, viewModel, LocalContext.current, location)


}


@Composable
fun MapHood(
    cameraPositionState: CameraPositionState,
    viewModel: HomeViewModel,
    context: Context,
    location: LatLng,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = "",
            onValueChange = {/*TODO make request*/ },
            modifier = Modifier.fillMaxWidth(),

            )


        LocationPicker(
            cameraPositionState,
            viewModel
        )
        FloatingActionButton(
            onClick = {
                moveCamera(
                    viewModel,
                    context,
                    location,
                    cameraPositionState
                )
            },
            Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_my_location_24),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun LocationPicker(cameraPositionState: CameraPositionState, viewModel: HomeViewModel) {
    if (!cameraPositionState.isMoving) {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.BottomCenter)) {
                Text(text = "Поехали! ")
            }
            val loc = viewModel.markerLocation.collectAsState()
            Text(text = loc.value.toString())
        }
    }

}


@Composable
fun LocationMarker(cameraPositionState: CameraPositionState, viewModel: HomeViewModel) {
    Marker(position = cameraPositionState.position.target)
    viewModel.onEvent(HomeEvent.MarkerLocationChanged(cameraPositionState.position.target))
}

fun moveCamera(
    viewModel: HomeViewModel,
    context: Context,
    location: LatLng,
    cameraPositionState: CameraPositionState,
) {
    viewModel.onEvent(HomeEvent.OnMyLocationClicked(context))
    cameraPositionState.move(
        com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
            location,
            16f
        )
    )

}