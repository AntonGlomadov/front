package com.haberturm.hitchhikingapp.ui.views

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
                .border(1.dp, MaterialTheme.colors.secondary, RoundedCornerShape(32.dp)),
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
        if(currentMarkerState.value is MarkerPicked.MarkerAPicked){
            Text(
                text = "Выберите начальную точку",
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray,
            )
        }else if(currentMarkerState.value is MarkerPicked.MarkerBPicked){
            Text(
                text = "Выберите конечную точку точку",
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray,
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter

        ) {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(HomeEvent.OnMyLocationClicked(context))
                    moveCamera(
                        location,
                        cameraPositionState,
                        coroutineScope
                    )
                },
                Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_my_location_24),
                    contentDescription = "find_user_location_button"
                )
            }
            LocationPicker(
                cameraPositionState,
                viewModel,
                coroutineScope
            )

        }

    }
}