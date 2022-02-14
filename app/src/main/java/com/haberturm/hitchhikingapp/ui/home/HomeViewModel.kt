package com.haberturm.hitchhikingapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
) : ViewModel(), RouteNavigator by routeNavigator  {
    var latitude by mutableStateOf<Double>(54.6944)  //TODO its will be better to show loading screen then the fake location
        private set
    var longitude by mutableStateOf<Double>(20.4981)
        private set

    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val lastKnownLocation = task.result
                latitude = lastKnownLocation.latitude
                longitude = lastKnownLocation.longitude
            } else {
                //TODO: handle error
            }
        }
    }


}