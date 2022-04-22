package com.haberturm.hitchhikingapp.ui.auth.login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.util.PhoneNumberVisualTransformation
import com.haberturm.hitchhikingapp.ui.util.Util
import com.haberturm.hitchhikingapp.ui.views.*

object LoginRoute : NavRoute<LoginViewModel> {
    override val route: String = "auth/"

    @Composable
    override fun Content(viewModel: LoginViewModel) = Login(viewModel)

    @Composable
    override fun viewModel(): LoginViewModel = hiltViewModel()

}

@Composable
fun Login(
    viewModel: LoginViewModel,
) {
    val arrangement = remember {
        mutableStateOf(Arrangement.Top)
    }
    if (viewModel.phoneFieldFocusState.collectAsState().value) { // on focus
        arrangement.value = Arrangement.Top
    } else {
        arrangement.value = Arrangement.SpaceBetween
    }
    val focusManager = LocalFocusManager.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = arrangement.value,
    ) {
        val configuration = LocalConfiguration.current
        val textFieldError = remember {
            mutableStateOf("")
        }
        if (viewModel.phoneFieldState.collectAsState().value is Util.TextFieldState.Failure) {
            textFieldError.value =
                (viewModel.phoneFieldState.collectAsState().value as Util.TextFieldState.Failure).error
        }
        ProperTextField(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = configuration.screenHeightDp.dp * 0.4f
                )
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.auth_field_height).value.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_phone_android_24),
                    contentDescription = "phone_icon"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            valueText = viewModel.phoneNumber.collectAsState().value,
            visualTransformation = PhoneNumberVisualTransformation(),
            isPhoneInput = true,
            placeholder = stringResource(R.string.enter_phone_placeholder),
            onFocus = fun(focusState: Boolean) {
                viewModel.onEvent(LoginEvent.OnPhoneFieldFocused(focusState))
            },
            onValueChange = fun(number: String) {
                viewModel.onEvent(LoginEvent.UpdateNumber(number))
            },
            error = textFieldError.value
        )
        Row(
            modifier = Modifier.wrapContentSize(align = Alignment.BottomCenter)
        ) {
            OvalButton(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginEvent.EnterNumber)
                },
                text = "Войти",
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp
                    )
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.auth_field_height).value.dp)
            )
        }
    }
}

