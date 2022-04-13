package com.haberturm.hitchhikingapp.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.hitchhikingapp.ui.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.util.Util
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

    private val _phoneFieldFocusState = MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val phoneFieldFocusState = _phoneFieldFocusState.asStateFlow()

    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _phoneFieldError = MutableStateFlow<NumberState>(NumberState.None) // true - show err, false - not show err :)
    val phoneFieldError = _phoneFieldError.asStateFlow()

    fun onEvent(event: AuthEvent){
        when(event){
            is AuthEvent.OnPhoneFieldFocused -> {
                _phoneFieldFocusState.value = event.focusState
            }
            is AuthEvent.UpdateNumber ->{
                _phoneNumber.value = event.number
                Log.i(TAG, phoneNumber.value)
            }
            is AuthEvent.EnterNumber -> {
                _phoneFieldError.value = Util.checkNumber(phoneNumber.value)
                if(phoneFieldError.value is NumberState.Success){
                    navigateToRoute(HomeRoute.get(0))
                }
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