package com.haberturm.hitchhikingapp.ui.screens.auth.reg

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.hitchhikingapp.data.network.backend.auth.pojo.SignUpRequest
import com.haberturm.hitchhikingapp.data.repositories.auth.AuthRepository
import com.haberturm.hitchhikingapp.data.repositories.home.HomeRepository
import com.haberturm.hitchhikingapp.ui.screens.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.util.Constants
import com.haberturm.hitchhikingapp.ui.util.ModelPreferencesManager
import com.haberturm.hitchhikingapp.ui.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeNavigator: RouteNavigator,
    private val repository: AuthRepository,
    private val homeRep: HomeRepository
) : ViewModel(), RouteNavigator by routeNavigator {

    private val _uiEvent = Channel<RegEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    //phone number
    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _phoneFieldFocusState =
        MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val phoneFieldFocusState = _phoneFieldFocusState.asStateFlow()

    private val _phoneFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val phoneFieldState = _phoneFieldState.asStateFlow()

    //name
    private val _name = MutableStateFlow<String>("")
    val name = _name.asStateFlow()

    private val _nameFieldFocusState =
        MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val nameFieldFocusState = _nameFieldFocusState.asStateFlow()

    private val _nameFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val nameFieldState = _nameFieldState.asStateFlow()

    //surname
    private val _surname = MutableStateFlow<String>("")
    val surname = _surname.asStateFlow()

    private val _surnameFieldFocusState =
        MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val surnameFieldFocusState = _surnameFieldFocusState.asStateFlow()

    private val _surnameFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val surnameFieldState = _nameFieldState.asStateFlow()

    //password
    private val _passwordFieldFocusState =
        MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val passwordFieldFocusState = _passwordFieldFocusState.asStateFlow()

    private val _passwordFieldState =
        MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
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

    private val _repeatPasswordFieldState =
        MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val repeatPasswordFieldState = _repeatPasswordFieldState.asStateFlow()

    private val _repeatPassword = MutableStateFlow<String>("")
    val repeatPassword = _repeatPassword.asStateFlow()

    private val _repeatPasswordVisibilityState =
        MutableStateFlow<Boolean>(false) // true - visible, false - invisible
    val repeatPasswordVisibilityState = _repeatPasswordVisibilityState.asStateFlow()

    //birth
    private val _birth = MutableStateFlow<String>("")
    val birth = _birth.asStateFlow()

    private val _birthFieldFocusState =
        MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
    val birthFieldFocusState = _birthFieldFocusState.asStateFlow()

    private val _birthFieldState = MutableStateFlow<Util.TextFieldState>(Util.TextFieldState.None)
    val birthFieldState = _birthFieldState.asStateFlow()

    //email
    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _emailFieldFocusState =
        MutableStateFlow<Boolean>(false) // true - onFocus, false - is not focused
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
            is RegEvent.UpdateNumber -> {
                _phoneNumber.value = event.number
            }
            //name
            is RegEvent.OnNameFieldFocused -> {
                _nameFieldFocusState.value = event.focusState
            }
            is RegEvent.UpdateName -> {
                _name.value = event.name
            }
            //surname
            is RegEvent.OnSurnameFieldFocused -> {
                _surnameFieldFocusState.value = event.focusState
            }
            is RegEvent.UpdateSurname -> {
                _surname.value = event.surname
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
                _repeatPasswordFieldState.value =
                    Util.repeatPasswordCheck(password.value, repeatPassword.value)
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
                ) {
                    try {
                        repository.signUp(
                            SignUpRequest(
                                name = name.value,
                                surname = surname.value,
                                email = email.value,
                                password = password.value,
                                birth = birth.value,
                                phoneNumber = phoneNumber.value,
                            )
                        )
                            .onEach {
                                when (it.code()) {
                                    201 -> {
                                        repository.checkPasswordInDB(
                                            number = phoneNumber.value,
                                            password = password.value
                                        )
                                            .onEach { token ->
                                                if (token.isSuccessful){
                                                    homeRep.insertUser(phoneNumber.value,password.value)
                                                    ModelPreferencesManager.put(token.body(), Constants.ACCESS_TOKEN)
                                                    navigateToRoute(HomeRoute.route)

                                                }else{
                                                    //todo handle error
                                                }
                                            }
                                            .launchIn(viewModelScope)
                                    }
                                    409 -> {
                                        sendUiEvent(RegEvent.Error("Пользавтатель с таким номером телефона или e-mail уже зарегистрирован"))
                                    }
                                }
                            }
                            .launchIn(viewModelScope)
                    } catch (e: Exception) {
                        //todo
                    }
                }
            }
        }
    }

    private fun getPhoneNumber(savedStateHandle: SavedStateHandle): String {
        return RegRoute.getArgFrom(savedStateHandle)
    }

    private fun sendUiEvent(event: RegEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}