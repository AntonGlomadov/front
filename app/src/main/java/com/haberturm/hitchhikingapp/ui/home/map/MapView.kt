package com.haberturm.hitchhikingapp.ui.home.map

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MapView(latitude: Double, longitude: Double) {
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView, latitude, longitude)
}

@SuppressLint("MissingPermission")
@Composable
private fun MapViewContainer(
    mapView: MapView,
    latitude: Double,
    longitude: Double
) {
    AndroidView(
        { mapView }
    ) { mapView ->
        CoroutineScope(Dispatchers.Main).launch {
            val map = mapView.awaitMap()
            map.uiSettings.apply {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = true
            }
            map.isMyLocationEnabled = true
            //val destination = LatLng(54.6944, 20.4981)
            val destination = LatLng(latitude, longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15f))
        }
    }
}