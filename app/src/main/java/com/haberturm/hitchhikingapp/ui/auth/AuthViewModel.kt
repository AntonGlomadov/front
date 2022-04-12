package com.haberturm.hitchhikingapp.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.hitchhikingapp.ui.home.HomeEvent
import com.haberturm.hitchhikingapp.ui.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


const val TAG = "PHONE_STATE"
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
) : ViewModel(), RouteNavigator by routeNavigator{


    private val _uiEvent = MutableSharedFlow<AuthEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _phoneFieldState = MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val phoneFieldState = _phoneFieldState.asStateFlow()

    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber = _phoneNumber.asStateFlow()

    fun onEvent(event: AuthEvent){
        when(event){
            is AuthEvent.OnPhoneFieldFocused -> {
                _phoneFieldState.value = event.focusState
            }
            is AuthEvent.UpdateNumber ->{
                _phoneNumber.value = event.number
                Log.i(TAG, phoneNumber.value)
            }

            else -> {
                Unit
            }
        }
    }

    private fun emitUiEvent(event: AuthEvent){
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun onAuthClicked(){
        navigateToRoute(HomeRoute.get(0))
    }

}