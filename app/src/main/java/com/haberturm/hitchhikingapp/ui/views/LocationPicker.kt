package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.home.HomeViewModel
import com.haberturm.hitchhikingapp.ui.util.Util.moveCamera
import kotlinx.coroutines.CoroutineScope

@Composable
fun LocationPicker(
    cameraPositionState: CameraPositionState,
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope
) {
    if (!cameraPositionState.isMoving) {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
//                    viewModel.onEvent(
//                        HomeEvent.NavigateToSearchDirection(
//                            location,
//                            cameraPositionState.position.target
//                        )
//                    )
                    placeMarkerOnClick(
                        placeMarkerFun = {viewModel.onEvent(HomeEvent.PlaceMarker)},
                        cameraPositionState = cameraPositionState,
                        coroutineScope = coroutineScope
                    )
                },
            )
            {
                Text(text = "Поехали! ")
            }
        }
    }
}

fun placeMarkerOnClick(
    placeMarkerFun: () -> Unit,
    cameraPositionState: CameraPositionState,
    coroutineScope: CoroutineScope
){
    placeMarkerFun()
    val currentCameraPosition = cameraPositionState.position.target
    val newLocation = LatLng(
        currentCameraPosition.latitude - 0.00009,  //чтобы избавиться ри перекрытия маркеров
        currentCameraPosition.longitude
    )
    moveCamera(
        newLocation,
        cameraPositionState,
        coroutineScope,
        isAnimated = false
    )


}
