package com.haberturm.hitchhikingapp.ui.views

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.home.HomeViewModel
import com.haberturm.hitchhikingapp.ui.home.MarkerPicked
import com.haberturm.hitchhikingapp.ui.util.Util.moveCamera
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect

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
        val vmMarkerLocation = viewModel.currentMarkerLocation.collectAsState()

        LaunchedEffect(key1 = true) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is HomeEvent.RelocateMarker -> {
                        moveCamera(
                            vmMarkerLocation.value,
                            cameraPositionState,
                            coroutineScope
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
                    "Выбрать начальную точку"
                )
            } else if (currentMarkerState.value is MarkerPicked.MarkerBPicked) {
                LocationPicker(
                    cameraPositionState,
                    viewModel,
                    coroutineScope,
                    "Выбрать конечную точку"
                )

            } else if(currentMarkerState.value is MarkerPicked.AllMarkersPlaced){
                LocationPicker(
                    cameraPositionState,
                    viewModel,
                    coroutineScope,
                    "Поехали!"
                )
            }


        }

    }
}