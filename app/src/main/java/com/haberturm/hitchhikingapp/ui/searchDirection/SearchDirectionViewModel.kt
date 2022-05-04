package com.haberturm.hitchhikingapp.ui.searchDirection

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.data.repositories.searchDirection.SearchDirectionRepository
import com.haberturm.hitchhikingapp.ui.screens.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.util.Util.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PointType {
    object Start : PointType()
    object End : PointType()
}

@HiltViewModel
class SearchDirectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeNavigator: RouteNavigator,
    private val repository: SearchDirectionRepository,
) : ViewModel(), RouteNavigator by routeNavigator {

    var startPoint: LatLng
        private set

    var endPoint: LatLng
        private set


//    private val _startPointFormattedAddress = MutableStateFlow<String>("s")
//    val startPointFormattedAddress: StateFlow<String> = _startPointFormattedAddress
//    private val _endPointFormattedAddress = MutableStateFlow<String>("e")
//    val endPointFormattedAddress: StateFlow<String> = _endPointFormattedAddress


    var startPointFormattedAddress by mutableStateOf("")
        private set
    var endPointFormattedAddress by mutableStateOf("")
        private set

    init {
        startPoint = getPoints(PointType.Start, savedStateHandle)
        endPoint = getPoints(PointType.End, savedStateHandle)
        getReverseGeocode(startPoint,endPoint)
    }

    private fun getReverseGeocode(startPoint: LatLng, endPoint:LatLng) {
        val startPointAsString = "${startPoint.latitude},${startPoint.longitude}"
        val endPointAsString = "${endPoint.latitude},${endPoint.longitude}"

        viewModelScope.launch {
            repository.getReverseGeocodeLocation(startPointAsString)
                .catch { e ->
                    Log.i("ARGS", "${e}")
                    //TODO ERROR AND STATUS
                }
                .collect { data ->
                    startPointFormattedAddress = data.toUiModel().formattedAddress.toString()

                }
            repository.getReverseGeocodeLocation(endPointAsString)
                .catch { e ->
                    //TODO ERROR AND STATUS
                }
                .collect { data ->
                    endPointFormattedAddress = data.toUiModel().formattedAddress.toString()
                }
        }
    }

    private fun getPoints(type: PointType, savedStateHandle: SavedStateHandle): LatLng {
        return when (type) {
            is PointType.Start -> {
                val lat = SearchDirectionRoute.getArgFrom(savedStateHandle, ArgKeys.START_POINT_LAT)
                val lng = SearchDirectionRoute.getArgFrom(savedStateHandle, ArgKeys.START_POINT_LNG)
                LatLng(lat.toDouble(), lng.toDouble())
            }
            is PointType.End -> {
                val lat = SearchDirectionRoute.getArgFrom(savedStateHandle, ArgKeys.END_POINT_LAT)
                val lng = SearchDirectionRoute.getArgFrom(savedStateHandle, ArgKeys.END_POINT_LNG)
                LatLng(lat.toDouble(), lng.toDouble())
            }
        }
    }


    private val _uiEvent = Channel<HomeEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: SearchDirectionEvent) {
        when (event) {
            is SearchDirectionEvent.OnNavigateUpClicked -> {
                Log.i("LOG_DEBUG_VM", "ON BACK PRESSED")
                // navigateToRoute(HomeRoute.get(0))
                navigateUp()
            }
        }
    }
}