package com.haberturm.hitchhikingapp.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.hitchhikingapp.data.repositories.auth.AuthRepository
import com.haberturm.hitchhikingapp.ui.auth.reg.RegRoute
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
class LoginViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val repository: AuthRepository,
) : ViewModel(), RouteNavigator by routeNavigator{

    private val _uiEvent = MutableSharedFlow<LoginEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _phoneFieldFocusState = MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val phoneFieldFocusState = _phoneFieldFocusState.asStateFlow()

    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _phoneFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val phoneFieldState = _phoneFieldState.asStateFlow()

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.OnPhoneFieldFocused -> {
                _phoneFieldFocusState.value = event.focusState
            }
            is LoginEvent.UpdateNumber ->{
                _phoneNumber.value = event.number
                Log.i(TAG, phoneNumber.value)
            }
            is LoginEvent.EnterNumber -> {
//                _phoneFieldState.value = Util.checkNumber(phoneNumber.value)
//                if(phoneFieldState.value is TextFieldState.Success){
//                    if(repository.checkNumberInDB(phoneNumber.value)){
//                        navigateToRoute(PasswordRoute.get(phoneNumber.value))
//                    }else{
//                        navigateToRoute(RegRoute.get(phoneNumber.value))
//                    }
//                }

                //TODO
                navigateToRoute(RegRoute.get("+79632909012"))

            }

            else -> {
                Unit
            }
        }
    }

    private fun emitUiEvent(event: LoginEvent){
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun onAuthClicked(){
        navigateToRoute(HomeRoute.get(0))
    }

}