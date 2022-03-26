package com.haberturm.hitchhikingapp.ui.home.map

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.home.*
import com.haberturm.hitchhikingapp.ui.nav.NavigationState
import com.haberturm.hitchhikingapp.ui.util.Util
import com.haberturm.hitchhikingapp.ui.views.SearchField
import kotlinx.coroutines.flow.collect
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
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
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
    var currentMarker = remember {
        mutableStateOf(R.drawable.a_marker40)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        googleMapOptionsFactory = {
            GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(location, 16f))
        },
        onPOIClick = {
            Log.d("TAG", "POI clicked: ${it.name}")
        },
    ) {


        val navState = viewModel.navigationState.collectAsState()
        LaunchedEffect(key1 = true, block = {                       //useless now
            if (navState.value == NavigationState.NavigateUp()) {
                moveCamera(viewModel.bMarkerLocation.value, cameraPositionState)
            }
        })
        val markerPlacedState = viewModel.markerPlacedState.collectAsState()
        Log.i("MARKER", "${markerPlacedState.value.aPlaced} ${markerPlacedState.value.bPlaced}")

        if (!markerPlacedState.value.aPlaced || !markerPlacedState.value.bPlaced) {
            MovingMarker(cameraPositionState, viewModel, currentMarker.value)
        }
        if (markerPlacedState.value.aPlaced) {
            Log.i("MARKER", "place a marker")
            val position = remember {
                mutableStateOf(cameraPositionState.position.target)
            }
            Marker(
                position = position.value,
                icon = BitmapDescriptorFactory.fromResource(R.drawable.a_marker40),
                onClick = {
                    moveCamera(position.value, cameraPositionState)
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
                    moveCamera(position.value, cameraPositionState)
                    viewModel.onEvent(HomeEvent.MakeMarkerMovable(B_MARKER_KEY))
                    currentMarker.value = R.drawable.b_marker40
                    true
                }
            )
        }

        LaunchedEffect(key1 = true) {
            viewModel.markerEvent.onEach { event ->
                when (event) {
                    is HomeEvent.MarkerPlaced -> {
                        Log.i("MARKER", "WHAT?1")
                        if (event.keyOfMarker == A_MARKER_KEY) {
                            Log.i("MARKER-event-vm", "in view")
                            currentMarker.value = R.drawable.b_marker40
                            Log.i("MARKER", "a current")
                        }
                        if (event.keyOfMarker == B_MARKER_KEY) {
                            currentMarker.value = R.drawable.a_marker40
                            Log.i("MARKER", "b current")
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
        val vmMarkerLocation = viewModel.currentMarkerLocation.collectAsState()

        LaunchedEffect(key1 = true) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is HomeEvent.RelocateMarker -> {
                        moveCamera(
                            vmMarkerLocation.value,
                            cameraPositionState
                        )
                    }
                    else -> {
                    }
                }
            }
        }
        SearchField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colors.secondary, RoundedCornerShape(32.dp)),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    contentDescription = ""
                )
            },
            onDone = fun(s: String) {
                viewModel.onEvent(HomeEvent.GetGeocodeLocation(s))
            }

        )

        LocationPicker(
            cameraPositionState,
            viewModel,
            location
        )
        FloatingActionButton(
            onClick = {
                viewModel.onEvent(HomeEvent.OnMyLocationClicked(context))
                moveCamera(
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
fun LocationPicker(
    cameraPositionState: CameraPositionState,
    viewModel: HomeViewModel,
    location: LatLng
) {
    if (!cameraPositionState.isMoving) {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {
//                    viewModel.onEvent(
//                        HomeEvent.NavigateToSearchDirection(
//                            location,
//                            cameraPositionState.position.target
//                        )
//                    )
                    viewModel.onEvent(HomeEvent.PlaceMarker)
                    val currentCameraPosition = cameraPositionState.position.target
                    val newLocation = LatLng(
                        currentCameraPosition.latitude-0.00001,
                        currentCameraPosition.longitude)
                    moveCamera(newLocation, cameraPositionState)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            {
                Text(text = "Поехали! ")
            }
            val loc = viewModel.bMarkerLocation.collectAsState()
            // Text(text = loc.value.toString())
        }
    }

}


@Composable
fun MovingMarker(
    cameraPositionState: CameraPositionState,
    viewModel: HomeViewModel,
    iconRes: Int
) {
    Marker(
        position = cameraPositionState.position.target,
        icon = BitmapDescriptorFactory.fromResource(iconRes),
    )
    viewModel.onEvent(HomeEvent.ObserveMovingMarkerLocation(cameraPositionState.position.target))
}

@Composable
fun UserLocationMarker(location: LatLng) {
    Marker(
        position = location,
        icon = Util.bitmapDescriptorFromVector(
            LocalContext.current,
            R.drawable.ic_baseline_my_location_24
        ),
        flat = true
    )


}

fun moveCamera(
    location: LatLng,
    cameraPositionState: CameraPositionState,
) {
    cameraPositionState.move(
        com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
            location,
            16f
        )
    )
}

@Preview
@Composable
fun TestSearch() {
    Box {
        TextField(
            value = "",
            onValueChange = {/*TODO make request*/ },
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

//@Composable
//fun OnMarkerClick(){
//
//}