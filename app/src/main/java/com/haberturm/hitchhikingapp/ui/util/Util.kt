package com.haberturm.hitchhikingapp.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.compose.material.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.Location
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.data.network.pojo.reverseGeocode.ReverseGeocodeResponse
import com.haberturm.hitchhikingapp.ui.model.GeocodeUiModel

object Util {
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

}

