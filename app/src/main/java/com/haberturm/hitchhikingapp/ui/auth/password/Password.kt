package com.haberturm.hitchhikingapp.ui.auth.password

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.nav.getOrThrow
import com.haberturm.hitchhikingapp.ui.util.Util
import com.haberturm.hitchhikingapp.ui.views.OvalButton
import com.haberturm.hitchhikingapp.ui.views.ProperTextField

const val PHONE_NUMBER = "PHONE_NUMBER"

object PasswordRoute : NavRoute<PasswordViewModel> {

    override val route = "password/{$PHONE_NUMBER}/"

    fun get(number: String): String = route.replace("{$PHONE_NUMBER}", number)

    fun getArgFrom(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<String>(PHONE_NUMBER)

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(PHONE_NUMBER) { type = NavType.StringType })

    @Composable
    override fun viewModel(): PasswordViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: PasswordViewModel) = Password(viewModel)
}

@Composable
fun Password(
    viewModel: PasswordViewModel
) {
    val arrangement = remember {
        mutableStateOf(Arrangement.Top)
    }
    if (viewModel.passwordFieldFocusState.collectAsState().value) { // on focus
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
        if (viewModel.passwordFieldState.collectAsState().value is Util.TextFieldState.Failure) {
            textFieldError.value =
                (viewModel.passwordFieldState.collectAsState().value as Util.TextFieldState.Failure).error
        }
        val isPasswordVisible = viewModel.passwordVisibilityState.collectAsState().value
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
                val image = if (isPasswordVisible)
                    painterResource(id = R.drawable.ic_baseline_visibility_off_24)
                else painterResource(id = R.drawable.ic_baseline_visibility_24)

                val description = if (isPasswordVisible) "Hide password" else "Show password"
                IconButton(
                    onClick = {
                        if (isPasswordVisible) {
                            viewModel.onEvent(PasswordEvent.OnPasswordVisibilityChange(false))
                        } else {
                            viewModel.onEvent(PasswordEvent.OnPasswordVisibilityChange(true))
                        }
                    }
                ) {
                    Icon(painter = image, contentDescription = description)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            valueText = viewModel.password.collectAsState().value,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = stringResource(R.string.enter_password_placeholder),
            onFocus = fun(focusState: Boolean) {
                viewModel.onEvent(PasswordEvent.OnPasswordFieldFocused(focusState))
            },
            onValueChange = fun(password: String) {
                viewModel.onEvent(PasswordEvent.UpdatePassword(password))
            },
            error = textFieldError.value,
            onDone = { viewModel.onEvent(PasswordEvent.EnterPassword) }
        )
        Row(
            modifier = Modifier.wrapContentSize(align = Alignment.BottomCenter)
        ) {
            OvalButton(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(PasswordEvent.EnterPassword)
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