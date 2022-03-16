package com.haberturm.hitchhikingapp.ui.model

import com.google.android.libraries.maps.model.LatLng

data class GeocodeUiModel(
    val formattedAddress: String?,
    val location: com.google.android.gms.maps.model.LatLng
)