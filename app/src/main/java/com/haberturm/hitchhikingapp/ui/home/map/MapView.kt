package com.haberturm.hitchhikingapp.ui.home.map

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.home.A_MARKER_KEY
import com.haberturm.hitchhikingapp.ui.home.B_MARKER_KEY
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.home.HomeViewModel
import com.haberturm.hitchhikingapp.ui.nav.NavigationState
import com.haberturm.hitchhikingapp.ui.util.Util.moveCamera
import com.haberturm.hitchhikingapp.ui.views.MapHood
import com.haberturm.hitchhikingapp.ui.views.MovingMarker
import com.haberturm.hitchhikingapp.ui.views.UserLocationMarker
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Composable
fun GoogleMapView(
    latitude: Double,
    longitude: Double,
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
    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false
            )
        )
    }
    val currentMarker = remember {
        mutableStateOf(R.drawable.a_marker40)
    }
    val coroutineScope = rememberCoroutineScope()

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        googleMapOptionsFactory = {
            GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(location, 16f))
        },
    ) {
        val navState = viewModel.navigationState.collectAsState()
        LaunchedEffect(key1 = true, block = {                       //useless now
            if (navState.value == NavigationState.NavigateUp()) {
                moveCamera(viewModel.currentMarkerLocation.value, cameraPositionState, coroutineScope)
            }
        })
        val markerPlacedState = viewModel.markerPlacedState.collectAsState()

        if (!markerPlacedState.value.aPlaced || !markerPlacedState.value.bPlaced) {
            MovingMarker(cameraPositionState, viewModel, currentMarker.value)
        }
        if (markerPlacedState.value.aPlaced) {
            val position = remember {
                mutableStateOf(cameraPositionState.position.target)
            }
            Marker(
                position = position.value,
                icon = BitmapDescriptorFactory.fromResource(R.drawable.a_marker40),
                onClick = {
                    moveCamera(
                        position.value,
                        cameraPositionState,
                        coroutineScope
                    )
                    viewModel.onEvent(HomeEvent.MakeMarkerMovable(A_MARKER_KEY))
                    currentMarker.value = R.drawable.a_marker40
                    true
                }
            )
        }
        if (markerPlacedState.value.bPlaced) {
            val position = remember {
                mutableStateOf(cameraPositionState.position.target)
            }
            Marker(
                position = position.value,
                icon = BitmapDescriptorFactory.fromResource(R.drawable.b_marker40),
                onClick = {
                    viewModel.onEvent(HomeEvent.MakeMarkerMovable(B_MARKER_KEY))
                    moveCamera(
                        position.value,
                        cameraPositionState,
                        coroutineScope
                    )
                    currentMarker.value = R.drawable.b_marker40
                    true
                }
            )
        }

        LaunchedEffect(key1 = true) {
            viewModel.markerEvent.onEach { event ->
                when (event) {
                    is HomeEvent.MarkerPlaced -> {
                        if (event.keyOfMarker == A_MARKER_KEY) {
                            currentMarker.value = R.drawable.b_marker40
                        }
                        if (event.keyOfMarker == B_MARKER_KEY) {
                            currentMarker.value = R.drawable.a_marker40
                        }
                    }
                    else -> {
                        Log.i("MARKER", "WHAT?")
                    }
                }
            }.launchIn(this)
        }
        UserLocationMarker(location = location)
    }
    MapHood(
        cameraPositionState,
        viewModel,
        LocalContext.current,
        location,
        coroutineScope
    )
}

