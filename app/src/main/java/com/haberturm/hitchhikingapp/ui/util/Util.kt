package com.haberturm.hitchhikingapp.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode.ReverseGeocodeResponse
import com.haberturm.hitchhikingapp.ui.model.GeocodeUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object Util {

    const val bMarkerDark = R.drawable.b_marker_dark
    const val aMarkerDark = R.drawable.a_marker_dark
    const val bMarkerLight = R.drawable.b_marker_light
    const val aMarkerLight = R.drawable.a_marker_light



    fun GeocodeLocationResponse.toUiModel(): GeocodeUiModel{
        return GeocodeUiModel(
            formattedAddress = results[0].formattedAddress,
            location = LatLng(
                results[0].geometry.location.lat,
                results[0].geometry.location.lng),
        )
    }


    fun ReverseGeocodeResponse.toUiModel(): GeocodeUiModel{
        return GeocodeUiModel(
            formattedAddress = results[0].formattedAddress,
            location = LatLng(
                results[0].geometry.location.lat,
                results[0].geometry.location.lng),
        )
    }


    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.BLUE, BlendModeCompat.SRC_ATOP)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    fun moveCamera(
        location: LatLng,
        cameraPositionState: CameraPositionState,
        coroutineScope: CoroutineScope,
        isAnimated: Boolean = true
    ) {
        if(isAnimated){
            coroutineScope.launch {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        location,
                        16f
                    )
                )
            }
        }else{
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    16f
                )
            )
        }

    }

}

