package com.haberturm.hitchhikingapp.ui.searchDirection

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

sealed class PointType {
    object Start : PointType()
    object End : PointType()
}

@HiltViewModel
class SearchDirectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeNavigator: RouteNavigator,
) : ViewModel(), RouteNavigator by routeNavigator {

    private var startPoint: LatLng
    private var endPoint: LatLng


    init {
//        val lat = SearchDirectionRoute.getArgFrom(savedStateHandle, ArgKeys.START_POINT_LAT)
//        val lng = SearchDirectionRoute.getArgFrom(savedStateHandle, ArgKeys.START_POINT_LNG)
        startPoint = getPoints(PointType.Start, savedStateHandle)
        endPoint = getPoints(PointType.End, savedStateHandle)
        Log.i("ARGS", "$startPoint $endPoint")
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