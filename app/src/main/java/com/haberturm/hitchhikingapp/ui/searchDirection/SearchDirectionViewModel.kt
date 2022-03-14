package com.haberturm.hitchhikingapp.ui.searchDirection

import android.util.Log
import androidx.lifecycle.ViewModel
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SearchDirectionViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
) : ViewModel(), RouteNavigator by routeNavigator{
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