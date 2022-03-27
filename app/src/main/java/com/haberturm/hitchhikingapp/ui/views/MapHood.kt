package com.haberturm.hitchhikingapp.ui.views

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.home.HomeViewModel
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

    Box(modifier = Modifier
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

        LocationPicker(
            cameraPositionState,
            viewModel,
            coroutineScope
        )
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
                contentDescription = ""
            )
        }
    }
}