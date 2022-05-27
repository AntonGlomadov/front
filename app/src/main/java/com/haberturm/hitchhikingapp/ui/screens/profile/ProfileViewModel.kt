package com.haberturm.hitchhikingapp.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.traceEventEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.data.repositories.home.HomeRepository
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.screens.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val routeNavigator: RouteNavigator,
    savedStateHandle: SavedStateHandle,
    val repository: HomeRepository
) : ViewModel(), RouteNavigator by routeNavigator {
    private val _dropDownState = MutableStateFlow(false)
    val dropDownState = _dropDownState.asStateFlow()

    private val _modeSwitchState = MutableStateFlow(false) // false - companion, true - driver
    val modeSwitchState = _modeSwitchState.asStateFlow()

    private val _userMode = MutableStateFlow<Constants.UserMode>(Constants.UserMode.Companion)
    val userMode = _userMode.asStateFlow()

    //additional dialog
    private val _showAdditionalRegistration =
        MutableStateFlow<Boolean>(false)
    val showAdditionalRegistration: StateFlow<Boolean> = _showAdditionalRegistration

    private val _carNumberTextValue = MutableStateFlow<String>("")
    val carNumberTextValue = _carNumberTextValue.asStateFlow()

    private val _carInfoTextValue = MutableStateFlow<String>("")
    val carInfoTextValue = _carInfoTextValue.asStateFlow()

    private val _carColorTextValue = MutableStateFlow<String>("")
    val carColorTextValue = _carColorTextValue.asStateFlow()

    private var wasDriver = false

    private val number: String = "+33333333335"
    init {
        when(getMode(savedStateHandle)){
            Constants.NavArgConst.DRIVER.arg -> {
                _userMode.value = Constants.UserMode.Driver
                _modeSwitchState.value = true
                wasDriver = true

            }
            Constants.NavArgConst.COMPANION.arg -> {
                _userMode.value = Constants.UserMode.Companion
                _modeSwitchState.value = false
            }
            else -> Unit
        }
    }

    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.UpdateDropDownState -> {
                _dropDownState.value = !dropDownState.value
            }
            is ProfileEvent.UpdateModeSwitchState -> {
                if (modeSwitchState.value == false){ //if companion try to change to driver
                    if (wasDriver){
                        _modeSwitchState.value = true
                    }else{
                        repository.checkIfDriverExist(number)
                            .onEach {
                                Log.i("CHECK-DRIVER", it.code().toString())
                                when (it.code()) {
                                    200 -> {
                                        wasDriver = true
                                        _modeSwitchState.value = true
                                    }
                                    403 -> {
                                        _showAdditionalRegistration.value = true

                                    }
                                }
                            }
                            .launchIn(viewModelScope)
                    }

                }else{ //if driver try to change to companion
                    _modeSwitchState.value = false
                }
                _userMode.value = if(modeSwitchState.value){
                    Constants.UserMode.Driver
                }else{
                    Constants.UserMode.Companion
                }
            }
            is ProfileEvent.NavigateTo -> {
                routeNavigator.navigateToRoute(event.route)
            }
            is ProfileEvent.UpdateCarNumberTextValue -> {
                _carNumberTextValue.value = event.textValue
            }
            is ProfileEvent.UpdateCarInfoTextValue -> {
                _carInfoTextValue.value = event.textValue
            }
            is ProfileEvent.UpdateCarColorTextValue -> {
                _carColorTextValue.value = event.textValue
            }
            is ProfileEvent.SendAdditionalInfo -> {
                repository.sendAdditionalInfo(
                    phoneNumber = number,
                    carNumber = carNumberTextValue.value,
                    carInfo = carInfoTextValue.value,
                    carColor = carColorTextValue.value,

                    )
                    .onEach { response ->
                        when (response.code()) {
                            200, 201 -> {
                                onEvent(ProfileEvent.UpdateModeSwitchState)
                            }
                            else -> {
                                Log.i("EXCEPTION-SEND-ADDINFO-VM", response.errorBody().toString())
                                //todo handle error
                            }
                        }
                    }
                    .launchIn(viewModelScope)


            }
            is ProfileEvent.OnDismissAdditionalInfo -> {
                _showAdditionalRegistration.value = false
                //onEvent(HomeEvent.ChangeUserMode(mode = Constants.UserMode.Companion))
            }
        }
    }

    private fun getMode(savedStateHandle: SavedStateHandle): String {
        return ProfileRoute.getArgFrom(savedStateHandle)
    }

    private fun sendAdditionalInfo(){

    }
}