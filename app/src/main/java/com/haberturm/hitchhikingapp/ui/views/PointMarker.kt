package com.haberturm.hitchhikingapp.ui.views

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker

@Composable
fun PointMarker(
    position: LatLng,
    icon: BitmapDescriptor?,
){
    Marker(
        position = position,
        icon = icon,
        onClick = {
            Log.i("marker", "lox")
            true}
    )
}