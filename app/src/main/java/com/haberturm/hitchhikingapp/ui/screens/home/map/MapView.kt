package com.haberturm.hitchhikingapp.ui.screens.home.map

import android.content.Context
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.screens.home.A_MARKER_KEY
import com.haberturm.hitchhikingapp.ui.screens.home.B_MARKER_KEY
import com.haberturm.hitchhikingapp.ui.screens.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.screens.home.HomeViewModel
import com.haberturm.hitchhikingapp.ui.nav.NavigationState
import com.haberturm.hitchhikingapp.ui.util.Util
import com.haberturm.hitchhikingapp.ui.util.Util.defaultZoom
import com.haberturm.hitchhikingapp.ui.util.Util.moveCamera
import com.haberturm.hitchhikingapp.ui.views.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun GoogleMapView(
    latitude: Double,
    longitude: Double,
    modifier: Modifier,
    onMapLoaded: () -> Unit,
    viewModel: HomeViewModel,
    context: Context,
) {
    val location = LatLng(latitude, longitude)
    Log.i("LOCATION", "${location}")
    // Observing and controlling the camera's state can be done with a CameraPositionState
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, defaultZoom)
    }

    val isDark = isSystemInDarkTheme()
    val mapProperties by remember {
        if (isDark) {
            mutableStateOf(
                MapProperties(
                    mapType = MapType.NORMAL,
                    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                        context,
                        R.raw.map_night_mode
                    )
                )
            )
        } else {
            mutableStateOf(MapProperties(mapType = MapType.NORMAL))
        }
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false,

                )
        )
    }

    val firstLaunch = viewModel.firstLaunch.collectAsState()

    val aMarker: Int by lazy { getAMarkerAsset(isDark) }
    val bMarker: Int by lazy { getBMarkerAsset(isDark) }


    if (firstLaunch.value) {
        viewModel.onEvent(HomeEvent.ChangeCurrentMarkerRes(aMarker))
        viewModel.onEvent(HomeEvent.ColorModeChanged(isDark))

    }
    LaunchedEffect(key1 = isDark, block = {
        viewModel.onEvent(HomeEvent.ColorModeChanged(isDark))
    })

//    LaunchedEffect(key1 = true, block = {
//        viewModel.uiEvent.collect { event ->
//            if(event is HomeEvent.ChangeUserMode){
//                moveCamera(location,cameraPositionState, this, false)
//            }
//        }
//    })
    val error = remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeEvent.RelocateMarker -> {
                    moveCamera(
                        event.location,
                        cameraPositionState,
                        this,
                        event.animation
                    )
                }
                is HomeEvent.ShowError -> {
                    error.value = event.e.message ?: "unknown error"
                }
                else -> {
                }
            }
        }
    }
    if (error.value.isNotEmpty()) {
        ErrorAlertDialog(
            title = "Ошибка",
            text = error.value,
            button1Text = "ok",
            button2Text = "ok"
        ) {
            viewModel.onEvent(HomeEvent.RecreateAfterError)
            error.value = ""
        }
    }
    val showAdditionalRegistration = viewModel.showAdditionalRegistration.collectAsState().value
    if (showAdditionalRegistration) {
        AdditionalInfDialog(
            carNumberTextValue = viewModel.carNumberTextValue.collectAsState().value,
            carNumberOnValueChange = fun(valueText: String) {
                viewModel.onEvent(HomeEvent.UpdateCarNumberTextValue(valueText))
            },
            carInfoTextValue = viewModel.carInfoTextValue.collectAsState().value,
            carInfoOnValueChange =fun(valueText: String) {
                viewModel.onEvent(HomeEvent.UpdateCarInfoTextValue(valueText))
            },
            carColorTextValue = viewModel.carColorTextValue.collectAsState().value,
            carColorOnValueChange = fun(valueText: String) {
                viewModel.onEvent(HomeEvent.UpdateCarColorTextValue(valueText))
            },
            sendAdditionalInfo = {viewModel.onEvent(HomeEvent.SendAdditionalInfo)},
            onDismiss = {viewModel.onEvent(HomeEvent.OnDismissAdditionalInfo)}
        )
    }


    val currentMarker = viewModel.currentMarkerRes.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        googleMapOptionsFactory = {
            GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(location, defaultZoom))
        },
    ) {
        val navState = viewModel.navigationState.collectAsState()
        LaunchedEffect(key1 = true, block = {                       //useless now
            if (navState.value == NavigationState.NavigateUp()) {
                moveCamera(
                    viewModel.currentMarkerLocation.value,
                    cameraPositionState,
                    coroutineScope
                )
            }
        })
        val markerPlacedState = viewModel.markerPlacedState.collectAsState()
        if (!markerPlacedState.value.aPlaced) {
            Circle(
                center = location,
                strokeColor = MaterialTheme.colors.secondaryVariant,
                radius = Util.startRadius,
            )
        }
        if (!markerPlacedState.value.aPlaced || !markerPlacedState.value.bPlaced) {
            MovingMarker(cameraPositionState, viewModel, currentMarker.value)
        }
        if (markerPlacedState.value.aPlaced) {
            val position = viewModel.aMarkerLocation.collectAsState()
            Marker(
                position = position.value,
                icon = BitmapDescriptorFactory.fromResource(aMarker),
                onClick = {
                    moveCamera(
                        position.value,
                        cameraPositionState,
                        coroutineScope
                    )
                    viewModel.onEvent(HomeEvent.MakeMarkerMovable(A_MARKER_KEY))
                    viewModel.onEvent(HomeEvent.ChangeCurrentMarkerRes(aMarker))
                    false
                }
            )
        }
        if (markerPlacedState.value.bPlaced) {
            val position = viewModel.bMarkerLocation.collectAsState()
            Marker(
                position = position.value,
                icon = BitmapDescriptorFactory.fromResource(bMarker),
                onClick = {
                    viewModel.onEvent(HomeEvent.MakeMarkerMovable(B_MARKER_KEY))
                    moveCamera(
                        position.value,
                        cameraPositionState,
                        coroutineScope
                    )
                    viewModel.onEvent(HomeEvent.ChangeCurrentMarkerRes(bMarker))
                    false
                }
            )
        }
        val showMarkerError = remember {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = true) {
            viewModel.markerEvent.onEach { event ->
                when (event) {
                    is HomeEvent.MarkerPlaced -> {
                        if (event.keyOfMarker == A_MARKER_KEY) {
                            viewModel.onEvent(HomeEvent.ChangeCurrentMarkerRes(bMarker))
                        }
                        if (event.keyOfMarker == B_MARKER_KEY) {
                            viewModel.onEvent(HomeEvent.ChangeCurrentMarkerRes(aMarker))
                        }
                    }
                    is HomeEvent.IsNotInRadius -> {
                        showMarkerError.value = true
                    }
                    else -> {
                        Log.i("MARKER", "WHAT?")
                    }
                }
            }.launchIn(this)
        }
        if (showMarkerError.value) {
            ErrorAlertDialog(
                title = "Точка не в радиусе",
                text = "Выбирите в радиусе",
                button1Text = "ok",
                button2Text = "ok"
            ) {
                showMarkerError.value = false
            }
        }

        //draw route
        val shouldShowDirection = viewModel.shouldShowDirection.collectAsState()
        if (shouldShowDirection.value) {
            val paths = viewModel.pathsList.collectAsState()
            paths.value.forEach { points ->
                Polyline(points = points)
            }
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

fun getAMarkerAsset(isDarkTheme: Boolean): Int {
    return if (isDarkTheme) {
        Util.aMarkerDark
    } else {
        Util.aMarkerLight
    }
}

fun getBMarkerAsset(isDarkTheme: Boolean): Int {
    return if (isDarkTheme) {
        Util.bMarkerDark
    } else {
        Util.bMarkerLight
    }
}

@Composable
fun ModeHood() {

}