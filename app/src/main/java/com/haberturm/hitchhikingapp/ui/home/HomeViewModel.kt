package com.haberturm.hitchhikingapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.data.network.ApiState
import com.haberturm.hitchhikingapp.data.repositories.home.HomeRepository
import com.haberturm.hitchhikingapp.data.repositories.home.HomeRepositoryEvent
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.searchDirection.SearchDirectionRoute
import com.haberturm.hitchhikingapp.ui.util.Util.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import user.userdb.UserEntity
import javax.inject.Inject

sealed class MarkerPicked {
    object MarkerAPicked : MarkerPicked()
    object MarkerBPicked : MarkerPicked()
    object AllMarkersPlaced : MarkerPicked()
}

data class MarkerPlacedState(
    val aPlaced: Boolean,
    val bPlaced: Boolean
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val repository: HomeRepository,
) : ViewModel(), RouteNavigator by routeNavigator {

    private val _uiEvent = Channel<HomeEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _markerEvent = MutableSharedFlow<HomeEvent>()
    val markerEvent = _markerEvent.asSharedFlow()

    var location: Flow<UserEntity> = repository.getUserLocation()
        private set

    private val _currentMarkerLocation = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val currentMarkerLocation: StateFlow<LatLng> = _currentMarkerLocation

    private var markerPicked: MarkerPicked = MarkerPicked.MarkerAPicked

    private val _bMarkerLocation = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val bMarkerLocation: StateFlow<LatLng> = _bMarkerLocation

    private val _aMarkerLocation = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val aMarkerLocation: StateFlow<LatLng> = _aMarkerLocation

    private val _markerPlacedState =
        MutableStateFlow<MarkerPlacedState>(MarkerPlacedState(aPlaced = false, bPlaced = false))
    val markerPlacedState: StateFlow<MarkerPlacedState> = _markerPlacedState


    private val _userLocationStatus =
        MutableStateFlow(HomeEvent.IsMapReady(isLocationReady = false, isMapReady = false))
    val userLocationStatus: StateFlow<HomeEvent.IsMapReady> = _userLocationStatus

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
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
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
                            _currentMarkerLocation.value = data.toUiModel().location
                            geocodeApiResponse.value = ApiState.Success(data)
                            sendUiEvent(HomeEvent.RelocateMarker)
                            Log.i("testapi", currentMarkerLocation.value.toString())
                        }
                }
            }

            is HomeEvent.PlaceMarker -> {
                if (markerPicked == MarkerPicked.MarkerBPicked) {
                    Log.i("MARKER-event-vm", "place b")
                    _bMarkerLocation.value = currentMarkerLocation.value
                    markerPicked = MarkerPicked.MarkerAPicked
                    _markerPlacedState.value = MarkerPlacedState(
                        aPlaced = markerPlacedState.value.aPlaced,
                        bPlaced = true
                    )
                    Log.i("MARKER-event-vm-placing", "$markerPicked")
                    emitMarkerEvent(HomeEvent.MarkerPlaced(B_MARKER_KEY))

                } else if (markerPicked == MarkerPicked.MarkerAPicked) {
                    Log.i("MARKER-event-vm", "place a")
                    _aMarkerLocation.value = currentMarkerLocation.value
                    markerPicked = MarkerPicked.MarkerBPicked
                    _markerPlacedState.value = MarkerPlacedState(
                        aPlaced = true,
                        bPlaced = markerPlacedState.value.bPlaced
                    )
                    Log.i("MARKER-event-vm-placing", "$markerPicked ")
                    emitMarkerEvent(HomeEvent.MarkerPlaced(A_MARKER_KEY))

                }
            }
            is HomeEvent.ObserveMovingMarkerLocation -> {
                _currentMarkerLocation.value = event.location
            }

            is HomeEvent.MakeMarkerMovable -> {
                viewModelScope.launch {
                    delay(1000)

                    if (event.keyOfMarker == A_MARKER_KEY) {
                        _currentMarkerLocation.value = aMarkerLocation.value
                        markerPicked = MarkerPicked.MarkerAPicked
                        _markerPlacedState.value = MarkerPlacedState(
                            aPlaced = false,
                            bPlaced = markerPlacedState.value.bPlaced
                        )
                    }
                    if (event.keyOfMarker == B_MARKER_KEY) {
                        _currentMarkerLocation.value = bMarkerLocation.value
                        markerPicked = MarkerPicked.MarkerBPicked
                        _markerPlacedState.value = MarkerPlacedState(
                            aPlaced = markerPlacedState.value.bPlaced,
                            bPlaced = false
                        )
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

    private fun emitMarkerEvent(event: HomeEvent) {
        viewModelScope.launch {
            _markerEvent.emit(event)
        }
    }

    private fun sendUiEvent(event: HomeEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)

        }
    }
}

