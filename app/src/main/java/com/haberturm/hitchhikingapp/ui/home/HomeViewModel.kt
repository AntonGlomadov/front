package com.haberturm.hitchhikingapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.haberturm.hitchhikingapp.data.repositories.HomeRepository
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val repository: HomeRepository,
) : ViewModel(), RouteNavigator by routeNavigator {
    var latitude by mutableStateOf<Double>(1.35)  //TODO its will be better to show loading screen then the fake location
        private set
    var longitude by mutableStateOf<Double>(103.87)
        private set

    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context) {
        viewModelScope.launch {
            try {
                repository.getUserLocationWithApi(context, viewModelScope)
                val location = repository.getUserLocation(0)
                if (location != null) {
                    latitude = location.latitude!!
                    longitude = location.longitude!!
                }
            } catch (e: Exception) {
                //TODO: Handle exception
                Log.i("LOCATION_ERR","LOCATION_ERR $e")
            }
        }
    }


}