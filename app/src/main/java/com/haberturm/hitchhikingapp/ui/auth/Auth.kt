package com.haberturm.hitchhikingapp.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.util.PhoneNumberVisualTransformation
import com.haberturm.hitchhikingapp.ui.views.*

object AuthRoute : NavRoute<AuthViewModel> {
    override val route: String = "auth/"

    @Composable
    override fun Content(viewModel: AuthViewModel) = Auth(viewModel)

    @Composable
    override fun viewModel(): AuthViewModel = hiltViewModel()

}

@Composable
fun Auth(
    viewModel: AuthViewModel,
) {
    val arrangement = remember {
        mutableStateOf(Arrangement.Top)
    }
    if (viewModel.phoneFieldState.collectAsState().value) { // on focus
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
        ProperTextField(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = configuration.screenHeightDp.dp * 0.4f
                )
                .fillMaxWidth()
                .border(BorderStroke(1.dp, MaterialTheme.colors.primary), RoundedCornerShape(32.dp))
                .height(50.dp),
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
                viewModel.onEvent(AuthEvent.OnPhoneFieldFocused(focusState))
            },
            onValueChange = fun(number: String) {
                viewModel.onEvent(AuthEvent.UpdateNumber(number))
            }
        )
        Row(
            modifier = Modifier.wrapContentSize(align = Alignment.BottomCenter)
        ) {
            OvalButton(
                onClick = viewModel::onAuthClicked,
                text = "Войти",
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 8.dp
                    )
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}

