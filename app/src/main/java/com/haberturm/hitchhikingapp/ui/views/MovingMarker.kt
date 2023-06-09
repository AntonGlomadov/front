package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.haberturm.hitchhikingapp.ui.screens.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.screens.home.HomeViewModel

@Composable
fun MovingMarker(
    cameraPositionState: CameraPositionState,
    viewModel: HomeViewModel,
    iconRes: Int
) {
    Marker(
        position = cameraPositionState.position.target,
        icon = BitmapDescriptorFactory.fromResource(iconRes),
        onClick = {false}
    )
    viewModel.onEvent(HomeEvent.ObserveMovingMarkerLocation(cameraPositionState.position.target))
}
