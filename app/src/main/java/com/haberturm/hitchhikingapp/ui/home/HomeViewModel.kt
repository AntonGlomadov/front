package com.haberturm.hitchhikingapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.data.network.ApiState
import com.haberturm.hitchhikingapp.data.repositories.home.HomeRepository
import com.haberturm.hitchhikingapp.data.repositories.home.HomeRepositoryEvent
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.searchDirection.SearchDirectionRoute
import com.haberturm.hitchhikingapp.ui.util.Util
import com.haberturm.hitchhikingapp.ui.util.Util.isInRadius
import com.haberturm.hitchhikingapp.ui.util.Util.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
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

    private val _currentUserMode = MutableStateFlow<UserMode>(UserMode.Undefined)
    val currentUserMode: StateFlow<UserMode> = _currentUserMode


    private val _uiEvent = Channel<HomeEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _markerEvent = MutableSharedFlow<HomeEvent>()
    val markerEvent = _markerEvent.asSharedFlow()

    //    var location: Flow<UserEntity> = repository.getUserLocation()
//        private set
    private val _location = MutableStateFlow<UserEntity>(UserEntity(0, 1.0, 1.0))
    val location: StateFlow<UserEntity> = _location


    private val _currentMarkerLocation = MutableStateFlow<LatLng>(LatLng(0.0, 0.0))
    val currentMarkerLocation: StateFlow<LatLng> = _currentMarkerLocation

    private var _markerPicked = MutableStateFlow<MarkerPicked>(MarkerPicked.MarkerAPicked)
    val markerPicked: StateFlow<MarkerPicked> = _markerPicked


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

    private val _currentMarkerRes = MutableStateFlow<Int>(R.drawable.a_marker_light)
    val currentMarkerRes: StateFlow<Int> = _currentMarkerRes

    private val _firstLaunch = MutableStateFlow<Boolean>(true)
    val firstLaunch: StateFlow<Boolean> = _firstLaunch

    private val _pathsList = MutableStateFlow<MutableList<List<LatLng>>>(mutableListOf())
    val pathsList: StateFlow<MutableList<List<LatLng>>> = _pathsList

    private val _shouldShowDirection = MutableStateFlow<Boolean>(false)
    val shouldShowDirection: StateFlow<Boolean> = _shouldShowDirection

    private val geocodeApiResponse: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    private val _isDark = MutableStateFlow<Boolean>(false)
    val isDark: StateFlow<Boolean> = _isDark

    init {
        viewModelScope.launch {
            repository.homeRepositoryEvent.collect { event ->
                Log.i("Event", "INIT_VM $event")
                when (event) {
                    is HomeRepositoryEvent.UserLocationStatus -> if (getLocationStatus(event)) {
                        repository.getUserLocation()
                            .onEach { userEntity ->
                                _location.value = userEntity
                                _userLocationStatus.value = HomeEvent.IsMapReady(
                                    isLocationReady = true,
                                    isMapReady = userLocationStatus.value.isMapReady
                                )
                            }
                            .launchIn(this)
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

            is HomeEvent.ChangeCurrentMarkerRes -> {
                _currentMarkerRes.value = event.res
                if (firstLaunch.value) {
                    _firstLaunch.value = false
                }

            }

            is HomeEvent.ColorModeChanged -> {
                _isDark.value = event.colorMode
                if (isDark.value) {
                    if (markerPicked.value == MarkerPicked.MarkerAPicked) {
                        _currentMarkerRes.value = Util.aMarkerDark
                    } else {
                        _currentMarkerRes.value = Util.bMarkerDark
                    }
                } else {
                    if (markerPicked.value == MarkerPicked.MarkerAPicked) {
                        _currentMarkerRes.value = Util.aMarkerLight
                    } else {
                        _currentMarkerRes.value = Util.bMarkerLight
                    }
                }
            }

            is HomeEvent.GetGeocodeLocation -> {
                geocodeApiResponse.value = ApiState.Loading
                Log.i("TESTAPI", "request clicked")
                viewModelScope.launch {
                    repository.getGeocodeLocation(event.address)
                        .catch { e ->
                            geocodeApiResponse.value = ApiState.Failure(e)
                            Log.i("API_FAIL", "$e")
                            //TODO ERROR AND STATUS
                        }
                        .collect { data ->
                            _currentMarkerLocation.value = data.toUiModel().location
                            geocodeApiResponse.value = ApiState.Success(data)
                            sendUiEvent(
                                HomeEvent.RelocateMarker(
                                    location = currentMarkerLocation.value,
                                    animation = true
                                )
                            )
                            Log.i("testapi", currentMarkerLocation.value.toString())
                        }
                }
            }

            is HomeEvent.PlaceMarker -> {
                if (markerPicked.value == MarkerPicked.MarkerBPicked) {
                    //place b
                    _bMarkerLocation.value = currentMarkerLocation.value
                    _markerPlacedState.value = MarkerPlacedState(
                        aPlaced = markerPlacedState.value.aPlaced,
                        bPlaced = true
                    )
                    if (markerPlacedState.value.aPlaced && markerPlacedState.value.bPlaced) {
                        _markerPicked.value = MarkerPicked.AllMarkersPlaced
                    } else {
                        _markerPicked.value = MarkerPicked.MarkerAPicked
                    }
                    emitMarkerEvent(HomeEvent.MarkerPlaced(B_MARKER_KEY))

                } else if (markerPicked.value == MarkerPicked.MarkerAPicked) {
                    //place a
                    if (!isInRadius(
                            center = LatLng(
                                location.value.latitude,
                                location.value.longitude
                            ),
                            point = LatLng(
                                currentMarkerLocation.value.latitude,
                                currentMarkerLocation.value.longitude
                            ),
                            radius = Util.startRadius
                        )
                    ) {
                        emitMarkerEvent(HomeEvent.IsNotInRadius)
                    } else {
                        _aMarkerLocation.value = currentMarkerLocation.value
                        _markerPlacedState.value = MarkerPlacedState(
                            aPlaced = true,
                            bPlaced = markerPlacedState.value.bPlaced
                        )
                        if (markerPlacedState.value.aPlaced && markerPlacedState.value.bPlaced) {
                            _markerPicked.value = MarkerPicked.AllMarkersPlaced
                        } else {
                            _markerPicked.value = MarkerPicked.MarkerBPicked
                        }
                        emitMarkerEvent(HomeEvent.MarkerPlaced(A_MARKER_KEY))
                    }
                } else {
                    viewModelScope.launch {
                        repository.getDirection(
                            destination = "${bMarkerLocation.value.latitude},${bMarkerLocation.value.longitude}",
                            origin = "${aMarkerLocation.value.latitude},${aMarkerLocation.value.longitude}"
                        )
                            .catch { e ->
                                ApiState.Failure(e)
                            }
                            .onEach { direction ->
                                direction.routes[0].legs[0].steps.forEach {
                                    _pathsList.value.add(PolyUtil.decode(it.polyline.points))
                                }
                                _shouldShowDirection.value = true
                            }
                            .launchIn(this)
                    }

                }
            }
            is HomeEvent.ObserveMovingMarkerLocation -> {
                _currentMarkerLocation.value = event.location
            }

            is HomeEvent.MakeMarkerMovable -> {
                viewModelScope.launch {
                    delay(1000) //delay for smooth animation

                    if (event.keyOfMarker == A_MARKER_KEY) {
                        _currentMarkerLocation.value = aMarkerLocation.value
                        _markerPicked.value = MarkerPicked.MarkerAPicked
                        _markerPlacedState.value = MarkerPlacedState(
                            aPlaced = false,
                            bPlaced = markerPlacedState.value.bPlaced
                        )
                    }
                    if (event.keyOfMarker == B_MARKER_KEY) {
                        _currentMarkerLocation.value = bMarkerLocation.value
                        _markerPicked.value = MarkerPicked.MarkerBPicked
                        _markerPlacedState.value = MarkerPlacedState(
                            aPlaced = markerPlacedState.value.bPlaced,
                            bPlaced = false
                        )
                    }
                }
            }

            is HomeEvent.ChangeUserMode -> {
                _currentUserMode.value = event.mode
                sendUiEvent(
                    HomeEvent.RelocateMarker(
                        location = LatLng(location.value.latitude, location.value.longitude),
                        animation = false
                    )
                )
                viewModelScope.launch {
                    Log.i("modes", "in handler $event")
                    delay(10)
                    if (currentUserMode.value is UserMode.Driver) {
                        _markerPicked.value = MarkerPicked.MarkerBPicked
                        _aMarkerLocation.value =
                            LatLng(location.value.latitude, location.value.longitude)
                        _markerPlacedState.value = MarkerPlacedState(
                            aPlaced = true,
                            bPlaced = false
                        )
                        emitMarkerEvent(HomeEvent.MarkerPlaced(A_MARKER_KEY))
                        if (isDark.value) {
                            _currentMarkerRes.value = Util.bMarkerDark
                        } else {
                            _currentMarkerRes.value = Util.bMarkerLight
                        }
                    } else {
                        _markerPicked.value = MarkerPicked.MarkerAPicked
                        _markerPlacedState.value = MarkerPlacedState(
                            aPlaced = false,
                            bPlaced = false
                        )
                        if (isDark.value) {
                            _currentMarkerRes.value = Util.aMarkerDark
                        } else {
                            _currentMarkerRes.value = Util.aMarkerLight
                        }
                    }
                }
            }
            else -> Unit
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

