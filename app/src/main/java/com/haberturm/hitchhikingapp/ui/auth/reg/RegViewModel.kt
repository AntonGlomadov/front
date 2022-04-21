package com.haberturm.hitchhikingapp.ui.auth.reg

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.haberturm.hitchhikingapp.data.repositories.auth.AuthRepository
import com.haberturm.hitchhikingapp.ui.auth.login.TAG
import com.haberturm.hitchhikingapp.ui.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeNavigator: RouteNavigator,
    private val repository: AuthRepository
) : ViewModel(), RouteNavigator by routeNavigator {
    //phone number
    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _phoneFieldFocusState = MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val phoneFieldFocusState = _phoneFieldFocusState.asStateFlow()

    private val _phoneFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val phoneFieldState = _phoneFieldState.asStateFlow()

    //name
    private val _name = MutableStateFlow<String>("")
    val name = _name.asStateFlow()

    private val _nameFieldFocusState = MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val nameFieldFocusState = _nameFieldFocusState.asStateFlow()

    private val _nameFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val nameFieldState = _nameFieldState.asStateFlow()

    //password
    private val _passwordFieldFocusState =
        MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val passwordFieldFocusState = _passwordFieldFocusState.asStateFlow()

    private val _passwordFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val passwordFieldState = _passwordFieldState.asStateFlow()

    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()

    private val _passwordVisibilityState =
        MutableStateFlow<Boolean>(false) // true - visible, false - invisible
    val passwordVisibilityState = _passwordVisibilityState.asStateFlow()

    //repeat password
    private val _repeatPasswordFieldFocusState =
        MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val repeatPasswordFieldFocusState = _repeatPasswordFieldFocusState.asStateFlow()

    private val _repeatPasswordFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val repeatPasswordFieldState = _repeatPasswordFieldState.asStateFlow()

    private val _repeatPassword = MutableStateFlow<String>("")
    val repeatPassword = _repeatPassword.asStateFlow()

    private val _repeatPasswordVisibilityState =
        MutableStateFlow<Boolean>(false) // true - visible, false - invisible
    val repeatPasswordVisibilityState = _repeatPasswordVisibilityState.asStateFlow()
    //birth
    private val _birth = MutableStateFlow<String>("")
    val birth = _birth.asStateFlow()

    private val _birthFieldFocusState = MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val birthFieldFocusState = _birthFieldFocusState.asStateFlow()

    private val _birthFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val birthFieldState = _birthFieldState.asStateFlow()
    //email
    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _emailFieldFocusState = MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val emailFieldFocusState = _emailFieldFocusState.asStateFlow()

    private val _emailFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val emailFieldState = _emailFieldState.asStateFlow()

    init {
        _phoneNumber.value = getPhoneNumber(savedStateHandle)
        Log.i("password", getPhoneNumber(savedStateHandle))
    }

    fun onEvent(event: RegEvent) {
        when (event) {
            //phone
            is RegEvent.OnPhoneFieldFocused -> {
                _phoneFieldFocusState.value = event.focusState
            }
            is RegEvent.UpdateNumber ->{
                _phoneNumber.value = event.number
                Log.i(TAG, phoneNumber.value)
            }
            //name
            is RegEvent.OnNameFieldFocused -> {
                _nameFieldFocusState.value = event.focusState
            }
            is RegEvent.UpdateName ->{
                _name.value = event.name
            }
            //password
            is RegEvent.UpdatePassword -> {
                _password.value = event.password
            }
            is RegEvent.OnPasswordFieldFocused -> {
                _passwordFieldFocusState.value = event.focusState
            }
            is RegEvent.OnPasswordVisibilityChange -> {
                _passwordVisibilityState.value = event.visibilityState
            }
            //repeat password
            is RegEvent.UpdateRepeatPassword -> {
                _repeatPassword.value = event.password
            }
            is RegEvent.OnRepeatPasswordFieldFocused -> {
                _repeatPasswordFieldFocusState.value = event.focusState
            }
            is RegEvent.OnRepeatPasswordVisibilityChange -> {
                _repeatPasswordVisibilityState.value = event.visibilityState
            }
            //birth
            is RegEvent.UpdateBirth -> {
                _birth.value = event.birth
            }
            is RegEvent.OnBirthFieldFocused -> {
                _birthFieldFocusState.value = event.focusState
            }
            //email
            is RegEvent.UpdateEmail -> {
                _email.value = event.email
            }
            is RegEvent.OnEmailFieldFocused -> {
                _emailFieldFocusState.value = event.focusState
            }

            is RegEvent.SignUp -> {
                _phoneFieldState.value = Util.checkNumber(phoneNumber.value)
                _passwordFieldState.value = Util.checkPassword(password.value)
                _repeatPasswordFieldState.value = Util.repeatPasswordCheck(password.value, repeatPassword.value)
                _birthFieldState.value = Util.checkDate(birth.value)
                _emailFieldState.value = Util.checkEmail(email.value)
                _nameFieldState.value = Util.checkName(name.value)
                if (
                    !(phoneFieldState.value != Util.TextFieldState.Success ||
                    passwordFieldState.value != Util.TextFieldState.Success ||
                    repeatPasswordFieldState.value != Util.TextFieldState.Success ||
                    birthFieldState.value != Util.TextFieldState.Success ||
                    emailFieldState.value != Util.TextFieldState.Success ||
                    nameFieldState.value != Util.TextFieldState.Success)
                ){
                    navigateToRoute(HomeRoute.get(0))
                }
            }
        }
    }


    private fun getPhoneNumber(savedStateHandle: SavedStateHandle): String {
        return RegRoute.getArgFrom(savedStateHandle)
    }


}