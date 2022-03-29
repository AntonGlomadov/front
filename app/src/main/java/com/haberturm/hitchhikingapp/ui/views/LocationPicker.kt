package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.home.HomeViewModel
import com.haberturm.hitchhikingapp.ui.util.Util.moveCamera
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationPicker(
    cameraPositionState: CameraPositionState,
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
    text: String = ""
) {
    if (!cameraPositionState.isMoving) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            CompositionLocalProvider(
                LocalMinimumTouchTargetEnforcement provides false,
            ) {
                LetsGoButton(
                    onClick = {
//                    viewModel.onEvent(
//                        HomeEvent.NavigateToSearchDirection(
//                            location,
//                            cameraPositionState.position.target
//                        )
//                    )
                        placeMarkerOnClick(
                            placeMarkerFun = { viewModel.onEvent(HomeEvent.PlaceMarker) },
                            cameraPositionState = cameraPositionState,
                            coroutineScope = coroutineScope
                        )
                    },
                    text = text
                )
            }
        }
    }
}

@Composable
@Preview
fun Prev(){
    if (!false) {
        Column(modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                modifier = Modifier,
                onClick = {
//                    viewModel.onEvent(
//                        HomeEvent.NavigateToSearchDirection(
//                            location,
//                            cameraPositionState.position.target
//                        )
//                    )

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
