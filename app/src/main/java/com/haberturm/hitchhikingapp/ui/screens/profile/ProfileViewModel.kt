package com.haberturm.hitchhikingapp.ui.screens.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val routeNavigator: RouteNavigator
) : ViewModel(), RouteNavigator by routeNavigator {
    private val _dropDownState = MutableStateFlow(false)
    val dropDownState = _dropDownState.asStateFlow()

    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.UpdateDropDownState -> {
                _dropDownState.value = !dropDownState.value
            }
        }
    }

}