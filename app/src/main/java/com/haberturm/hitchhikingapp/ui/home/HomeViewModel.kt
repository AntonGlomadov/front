package com.haberturm.hitchhikingapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.data.network.ApiState
import com.haberturm.hitchhikingapp.data.network.pojo.geocode.GeocodeLocationResponse
import com.haberturm.hitchhikingapp.data.repositories.home.HomeRepository
import com.haberturm.hitchhikingapp.data.repositories.home.HomeRepositoryEvent
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.searchDirection.SearchDirectionRoute
import com.haberturm.hitchhikingapp.ui.util.Util.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import user.userdb.UserEntity
import javax.inject.Inject

sealed class MarkerPicked {
    object MarkerAPicked : MarkerPicked()
    object MarkerBPicked : MarkerPicked()
}


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val repository: HomeRepository,
) : ViewModel(), RouteNavigator by routeNavigator {

    private val _uiEvent = Channel<HomeEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var location: Flow<UserEntity> = repository.getUserLocation()
        private set

    private val _markerLocation = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val markerLocation: StateFlow<LatLng> = _markerLocation

    private val _userLocationStatus =
        MutableStateFlow(HomeEvent.IsMapReady(isLocationReady = false, isMapReady = false))
    val userLocationStatus: StateFlow<HomeEvent.IsMapReady> = _userLocationStatus

    private val _markerPicked =
        MutableStateFlow(MarkerPicked.MarkerBPicked)
    val markerPicked: StateFlow<MarkerPicked> = _markerPicked

    private val geocodeApiResponse: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)



    init {
        viewModelScope.launch {
            repository.homeRepositoryEvent.collect { event ->
                Log.i("Event", "INIT_VM $event")
                when (event) {
                    is HomeRepositoryEvent.UserLocationStatus -> if (getLocationStatus(event)) {
                        //location = repository.getUserLocation()
                        Log.i("LOCATION_Init", location.toString())
                        _userLocationStatus.value = HomeEvent.IsMapReady(
                            isLocationReady = true,
                            isMapReady = userLocationStatus.value.isMapReady
                        )
                    }
                }
            }
            location.collect {
                _markerLocation.value = LatLng(it.latitude, it.longitude)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.MarkerLocationChanged -> {
                _markerLocation.value = event.location
            }
            is HomeEvent.MapReady -> {
                Log.i("Event", "ON_EVENT_VM $event")
                _userLocationStatus.value = HomeEvent.IsMapReady(
                    isLocationReady = userLocationStatus.value.isLocationReady,
                    isMapReady = true
                )
            }
            is HomeEvent.NavigateToSearchDirection -> {
                navigateToRoute(
                    SearchDirectionRoute.get(
                        event.startLocation.latitude.toString(),
                        event.startLocation.longitude.toString(),
                        event.endLocation.latitude.toString(),
                        event.endLocation.longitude.toString()
                    )
                )
            }
            is HomeEvent.OnMyLocationClicked -> {
                getUserLocation(event.context)
            }

            is HomeEvent.GetGeocodeLocation -> {
                geocodeApiResponse.value = ApiState.Loading
                Log.i("TESTAPI", "request clicked")
                viewModelScope.launch {
                    repository.getGeocodeLocation(event.address)
                        .catch { e ->
                            geocodeApiResponse.value = ApiState.Failure(e)
                            //TODO ERROR AND STATUS
                        }
                        .collect { data ->
                            _markerLocation.value = data.toUiModel().location
                            geocodeApiResponse.value = ApiState.Success(data)
                            sendUiEvent(HomeEvent.RelocateMarker)
                            Log.i("testapi", markerLocation.value.toString())
                        }
                }
            }
            else -> {
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

    private fun sendUiEvent(event: HomeEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

