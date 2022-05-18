package com.haberturm.hitchhikingapp.ui.screens.auth.password

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.hitchhikingapp.data.repositories.auth.AuthRepository
import com.haberturm.hitchhikingapp.ui.screens.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import com.haberturm.hitchhikingapp.ui.util.Util
import com.haberturm.hitchhikingapp.ui.util.Util.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeNavigator: RouteNavigator,
    private val repository: AuthRepository
) : ViewModel(), RouteNavigator by routeNavigator {

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

    private val number: String

    init {
        number = getPhoneNumber(savedStateHandle)
    }

    fun onEvent(event: PasswordEvent) {
        when (event) {
            is PasswordEvent.UpdatePassword -> {
                _password.value = event.password
            }
            is PasswordEvent.OnPasswordFieldFocused -> {
                _passwordFieldFocusState.value = event.focusState
            }
            is PasswordEvent.OnPasswordVisibilityChange -> {
                _passwordVisibilityState.value = event.visibilityState
            }

            is PasswordEvent.EnterPassword -> {
                try {
                    repository.checkPasswordInDB(
                            number = number,
                            password = password.value
                        )
                        .onEach { response ->
                            Log.i("TOKEN", "$response")
                            if (response.isSuccessful){
                                //_passwordFieldState.value = Util.TextFieldState.Success
                            }else{
                                val errorMsg = response.errorBody()
                                val code = response.code()
                                response.errorBody()?.close()
                                if(code == 401){
                                    _passwordFieldState.value = Util.TextFieldState.Failure("Неверный пароль!")
                                }
                            }
                        }
                        .launchIn(viewModelScope)
                } catch (e: Exception) {
                    Log.i("TOKEN", "$e")
                }
                //_passwordFieldState.value = repository.checkPasswordInDB(password.value)
                if (passwordFieldState.value is Util.TextFieldState.Success) {
                    navigateToRoute(HomeRoute.route)
                }
            }

        }

    }


    private fun getPhoneNumber(savedStateHandle: SavedStateHandle): String {
        return PasswordRoute.getArgFrom(savedStateHandle)
    }


}