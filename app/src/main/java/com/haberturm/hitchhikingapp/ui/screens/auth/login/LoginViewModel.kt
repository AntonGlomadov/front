package com.haberturm.hitchhikingapp.ui.screens.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.hitchhikingapp.data.repositories.auth.AuthRepository
import com.haberturm.hitchhikingapp.ui.screens.auth.password.PasswordRoute
import com.haberturm.hitchhikingapp.ui.screens.auth.reg.RegRoute
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
            }
            is LoginEvent.EnterNumber -> {
                _phoneFieldState.value = Util.checkNumber(phoneNumber.value)
                if(phoneFieldState.value is Util.TextFieldState.Success){
                    repository.checkNumberInDB(phoneNumber.value)
                        .onEach {
                            when(it.code()){
                                200, 403 -> {
                                    navigateToRoute(PasswordRoute.get(phoneNumber.value))
                                }
                                404 -> {
                                    delay(200) // for fix strange padding bug
                                    navigateToRoute(RegRoute.get(phoneNumber.value))
                                }
                            }
                        }
                        .launchIn(viewModelScope)
//                    if(repository.checkNumberInDB(phoneNumber.value)){
//                        Log.i(TAG, "to password")
//                        navigateToRoute(PasswordRoute.get(phoneNumber.value))
//                    }else{
//                        viewModelScope.launch {
//                            delay(250)  // for fix strange padding bug
//                            navigateToRoute(RegRoute.get(phoneNumber.value))
//                        }
//                    }
                }
            }
        }
    }

    private fun emitUiEvent(event: LoginEvent){
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}