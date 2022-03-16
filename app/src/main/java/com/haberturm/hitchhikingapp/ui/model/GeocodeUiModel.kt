package com.haberturm.hitchhikingapp.ui.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeocodeUiModel(
    val formattedAddress: String?,
    val location: LatLng
): Parcelable
