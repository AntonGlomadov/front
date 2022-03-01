package com.haberturm.hitchhikingapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.data.repositories.HomeRepository
import com.haberturm.hitchhikingapp.data.repositories.HomeRepositoryEvent
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import user.userdb.UserEntity
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val repository: HomeRepository,
) : ViewModel(), RouteNavigator by routeNavigator {


    private val _uiEvent = Channel<HomeEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var location: Flow<UserEntity> = flowOf()
        private set

    private val _markerLocation = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val markerLocation: StateFlow<LatLng> = _markerLocation


    private val _userLocationStatus =
        MutableStateFlow(HomeEvent.IsMapReady(isLocationReady = false, isMapReady = false))
    val userLocationStatus: StateFlow<HomeEvent.IsMapReady> = _userLocationStatus

    init {
        viewModelScope.launch {
            repository.homeRepositoryEvent.collect { event ->
                Log.i("Event", "${event}")
                when (event) {
                    is HomeRepositoryEvent.UserLocationStatus -> if (getLocationStatus(event)) {
                        location = repository.getUserLocation()
                        _userLocationStatus.value = HomeEvent.IsMapReady(
                            isLocationReady = true,
                            isMapReady = userLocationStatus.value.isMapReady
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {

            is HomeEvent.MarkerLocationChanged -> {
                Log.i("Event", event.toString())
                _markerLocation.value = event.location
            }
            is HomeEvent.MapReady -> {
                Log.i("Event", event.toString())
                _userLocationStatus.value = HomeEvent.IsMapReady(
                    isLocationReady = userLocationStatus.value.isLocationReady,
                    isMapReady = true
                )
                Log.i("Event", _userLocationStatus.value.toString())
            }
        }
    }


    private fun getLocationStatus(event: HomeRepositoryEvent.UserLocationStatus): Boolean {
        return event.isDone
    }


    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context) {
        viewModelScope.launch {
            try {
                repository.getUserLocationWithApi(context, viewModelScope)
            } catch (e: Exception) {
                //TODO: Handle exception
                Log.i("LOCATION_ERR", "LOCATION_ERR $e")
            }
        }
    }


}