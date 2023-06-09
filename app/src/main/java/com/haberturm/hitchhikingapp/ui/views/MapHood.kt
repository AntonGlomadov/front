package com.haberturm.hitchhikingapp.ui.views

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.screens.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.screens.home.HomeViewModel
import com.haberturm.hitchhikingapp.ui.screens.home.MarkerPicked
import com.haberturm.hitchhikingapp.ui.util.Constants
import com.haberturm.hitchhikingapp.ui.util.Util
import com.haberturm.hitchhikingapp.ui.util.Util.moveCamera
import kotlinx.coroutines.CoroutineScope

@Composable
fun MapHood(
    cameraPositionState: CameraPositionState,
    viewModel: HomeViewModel,
    context: Context,
    location: LatLng,
    coroutineScope: CoroutineScope
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {


        ProperTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(32.dp)),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    contentDescription = "search_icon"
                )
            },
            onDone = fun(s: String) {
                viewModel.onEvent(HomeEvent.GetGeocodeLocation(s))
            }

        )

        val currentMarkerState = viewModel.markerPicked.collectAsState()
        if (currentMarkerState.value is MarkerPicked.MarkerAPicked) {
            StrokeText("Выберите начальную точку")
        } else if (currentMarkerState.value is MarkerPicked.MarkerBPicked) {
            StrokeText("Выберите конечную точку")
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter

        ) {
            val checkedState = remember { mutableStateOf(false) } // true - driver, false - companion
            if (viewModel.currentUserMode.collectAsState().value is Constants.UserMode.Driver) {
                checkedState.value = true
            } else {
                checkedState.value = false
            }
            Switch(
                checked = checkedState.value,
                onCheckedChange = {
                    val mode = if(!checkedState.value){
                        Constants.UserMode.Driver
                    }else{
                        Constants.UserMode.Companion
                    }
                    Log.i("modes", "$mode")
                    viewModel.onEvent(HomeEvent.ChangeUserMode(mode))
                },
                modifier = Modifier.align(Alignment.BottomStart)
            )

            OutlinedButton(
                onClick = {
                    viewModel.onEvent(HomeEvent.OnMyLocationClicked(context))
                    moveCamera(
                        location,
                        cameraPositionState,
                        coroutineScope
                    )
                },
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.BottomEnd),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colors.primary),

                ) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_my_location_24),
                    contentDescription = "find_user_location_button",
                    tint = MaterialTheme.colors.primary,
                )
            }
            if (currentMarkerState.value is MarkerPicked.MarkerAPicked) {
                LocationPicker(
                    cameraPositionState,
                    viewModel,
                    coroutineScope,
                    "Выбрать начальную точку",
                    true
                )
            } else if (currentMarkerState.value is MarkerPicked.MarkerBPicked) {
                LocationPicker(
                    cameraPositionState,
                    viewModel,
                    coroutineScope,
                    "Выбрать конечную точку",
                    true
                )

            } else if (currentMarkerState.value is MarkerPicked.AllMarkersPlaced) {
                val firstPoint = viewModel.aMarkerLocation.collectAsState().value
                val secondPoint = viewModel.bMarkerLocation.collectAsState().value
                val bounds = Util.setRightBound(
                    firstPoint,
                    secondPoint
                )
                LocationPicker(
                    cameraPositionState,
                    viewModel,
                    coroutineScope,
                    "Поехали!",
                    false,
                    bounds,
                )
            }
        }
    }
}