package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.util.Util

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