package com.haberturm.hitchhikingapp.ui.auth.reg

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.nav.getOrThrow
import com.haberturm.hitchhikingapp.ui.util.PhoneNumberVisualTransformation
import com.haberturm.hitchhikingapp.ui.util.Util
import com.haberturm.hitchhikingapp.ui.views.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val PHONE_NUMBER = "PHONE_NUMBER"

object RegRoute : NavRoute<RegViewModel> {

    override val route = "reg/{$PHONE_NUMBER}/"

    fun get(number: String): String = route.replace("{$PHONE_NUMBER}", number)

    fun getArgFrom(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<String>(PHONE_NUMBER)

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(PHONE_NUMBER) { type = NavType.StringType })

    @Composable
    override fun viewModel(): RegViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: RegViewModel) = Reg(viewModel)

}

@Composable
private fun Reg(viewModel: RegViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            contentDescription = "NavigateUP"
                        )
                    }
                },
                title = {
                    Text(
                        text = "Регистрация",
                        color = MaterialTheme.colors.onBackground
                    )
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary,
            )
        },
        content = {
            val error = remember {
                mutableStateOf("")
            }
            LaunchedEffect(key1 = true, block = {
                viewModel.uiEvent.collect{event->
                    when(event){
                        is RegEvent.Error ->{
                            error.value = event.error
                        }
                        else -> {Unit}
                    }
                }
            })

            if(error.value.isNotEmpty()){
                ErrorAlertDialog(
                    title = "Ошибка",
                    text = error.value,
                    button1Text = "ok",
                    button2Text = "ok"
                ) {
                    error.value = ""
                }
            }
            val focusManager = LocalFocusManager.current
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    Modifier
                        .imePadding()
                ) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colors.primary))
                    Text(
                        text = "Упс, мы не нашли вашего номера в базе данных. Зарегитрируйтесь.\nВсе поля обязательны для заполнения.",
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                    PhoneNumberTextField(viewModel = viewModel)
                    //name
                    NameTextField(
                        fieldState = viewModel.nameFieldState.collectAsState().value,
                        valueText = viewModel.name.collectAsState().value,
                        onFocus = fun(focusState: Boolean) {
                            viewModel.onEvent(RegEvent.OnNameFieldFocused(focusState))
                        },
                        onValueChange = fun(name: String) {
                            viewModel.onEvent(RegEvent.UpdateName(name))
                        },
                        placeholder = stringResource(id = R.string.enter_name_placeholder)
                    )
                    //surname
                    NameTextField(
                        fieldState = viewModel.surnameFieldState.collectAsState().value,
                        valueText = viewModel.surname.collectAsState().value,
                        onFocus = fun(focusState: Boolean) {
                            viewModel.onEvent(RegEvent.OnSurnameFieldFocused(focusState))
                        },
                        onValueChange = fun(surname: String) {
                            viewModel.onEvent(RegEvent.UpdateSurname(surname))
                        },
                        placeholder = stringResource(id = R.string.enter_surname_placeholder)
                    )
                    //password
                    PasswordTextField(
                        fieldState = viewModel.passwordFieldState.collectAsState().value,
                        visibilityState = viewModel.passwordVisibilityState.collectAsState().value,
                        changeVisibility = fun(visibilityState: Boolean) {
                            viewModel.onEvent(RegEvent.OnPasswordVisibilityChange(visibilityState))
                        },
                        password = viewModel.password.collectAsState().value,
                        onFocus = fun(focusState: Boolean) {
                            viewModel.onEvent(RegEvent.OnPasswordFieldFocused(focusState))
                        },
                        onValueChange = fun(password: String) {
                            viewModel.onEvent(RegEvent.UpdatePassword(password))
                        },
                        placeholder = stringResource(id = R.string.enter_password_placeholder),
                        isFocused = viewModel.passwordFieldFocusState.collectAsState().value
                    )

                    //repeat password
                    PasswordTextField(
                        fieldState = viewModel.repeatPasswordFieldState.collectAsState().value,
                        visibilityState = viewModel.repeatPasswordVisibilityState.collectAsState().value,
                        changeVisibility = fun(visibilityState: Boolean) {
                            viewModel.onEvent(
                                RegEvent.OnRepeatPasswordVisibilityChange(
                                    visibilityState
                                )
                            )
                        },
                        password = viewModel.repeatPassword.collectAsState().value,
                        onFocus = fun(focusState: Boolean) {
                            viewModel.onEvent(RegEvent.OnRepeatPasswordFieldFocused(focusState))
                        },
                        onValueChange = fun(password: String) {
                            viewModel.onEvent(RegEvent.UpdateRepeatPassword(password))
                        },
                        placeholder = stringResource(id = R.string.repeat_password_placeholder),
                        isFocused = viewModel.repeatPasswordFieldFocusState.collectAsState().value
                    )
                    BirthTextField(viewModel = viewModel)
                    EmailTextField(viewModel = viewModel)
                }

                Row(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.BottomCenter),
                ) {
                    OvalButton(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.onEvent(RegEvent.SignUp)
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
    )
}


@Composable
private fun PhoneNumberTextField(viewModel: RegViewModel) {
    val numberFieldError = remember {
        mutableStateOf("")
    }
    if (viewModel.phoneFieldState.collectAsState().value is Util.TextFieldState.Failure) {
        numberFieldError.value =
            (viewModel.phoneFieldState.collectAsState().value as Util.TextFieldState.Failure).error
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProperTextField(
            modifier = Modifier
                .padding(
                    8.dp
                )
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.auth_field_height).value.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_phone_android_24),
                    contentDescription = "phone_icon"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            valueText = viewModel.phoneNumber.collectAsState().value,
            visualTransformation = PhoneNumberVisualTransformation(),
            isPhoneInput = true,
            placeholder = stringResource(R.string.enter_phone_placeholder),
            onFocus = fun(focusState: Boolean) {
                viewModel.onEvent(RegEvent.OnPhoneFieldFocused(focusState))
            },
            onValueChange = fun(number: String) {
                viewModel.onEvent(RegEvent.UpdateNumber(number))
            },
            error = numberFieldError.value
        )
        if (viewModel.phoneFieldFocusState.collectAsState().value && numberFieldError.value.isEmpty()) {
            HintText(
                text = "Введите номер в формате +(код страны)(номер)",
            )
        }
    }
}

@Composable
private fun NameTextField(
    fieldState: Util.TextFieldState,
    valueText: String,
    onFocus: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val nameFieldError = remember {
        mutableStateOf("")
    }
    if (fieldState is Util.TextFieldState.Failure) {
        nameFieldError.value =
            (fieldState as Util.TextFieldState.Failure).error
    }
    ProperTextField(
        modifier = Modifier
            .padding(
                8.dp
            )
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.auth_field_height).value.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_account_circle_24),
                contentDescription = "account_icon"
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        valueText = valueText,
        isPhoneInput = false,
        placeholder = placeholder,
        onFocus = onFocus,
        onValueChange = onValueChange,
        error = nameFieldError.value
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PasswordTextField(
    fieldState: Util.TextFieldState,
    visibilityState: Boolean,
    changeVisibility: (Boolean) -> Unit,
    password: String,
    onFocus: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isFocused: Boolean

) {
    val passwordFieldError = remember {
        mutableStateOf("")
    }
    if (fieldState is Util.TextFieldState.Failure) {
        passwordFieldError.value =
            (fieldState as Util.TextFieldState.Failure).error
    }
    val isPasswordVisible = visibilityState
    val  bringIntoViewRequester = BringIntoViewRequester()
    val coroutineScope = rememberCoroutineScope()
    Column() {
        ProperTextField(
            modifier = Modifier
                .padding(
                    8.dp
                )
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.auth_field_height).value.dp)
                .bringIntoViewRequester(bringIntoViewRequester),
            leadingIcon = {
                val image = if (isPasswordVisible)
                    painterResource(id = R.drawable.ic_baseline_visibility_off_24)
                else painterResource(id = R.drawable.ic_baseline_visibility_24)

                val description = if (isPasswordVisible) "Hide password" else "Show password"
                IconButton(
                    onClick = {
                        if (isPasswordVisible) {
                            changeVisibility(false)
                        } else {
                            changeVisibility(true)
                        }
                    }
                ) {
                    Icon(
                        painter = image,
                        contentDescription = description,
                        tint = MaterialTheme.colors.primary
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            valueText = password,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = placeholder,
            onFocus = {
                onFocus(it)
                coroutineScope.launch {
                    delay(250)
                    bringIntoViewRequester.bringIntoView()
                }

            },
            onValueChange = onValueChange,
            error = passwordFieldError.value,
            onDone = { }
        )
        if (isFocused && passwordFieldError.value.isEmpty()) {
            HintText(
                text = "Пароль должен состоять из 8-16 символов и содержать как минимум одну цифру",
            )
        }
    }
}

@Composable
fun BirthTextField(viewModel: RegViewModel) {
    val birthFieldError = remember {
        mutableStateOf("")
    }
    if (viewModel.birthFieldState.collectAsState().value is Util.TextFieldState.Failure) {
        birthFieldError.value =
            (viewModel.birthFieldState.collectAsState().value as Util.TextFieldState.Failure).error
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProperTextField(
            modifier = Modifier
                .padding(
                    8.dp
                )
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.auth_field_height).value.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_today_24),
                    contentDescription = "birthday"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            valueText = viewModel.birth.collectAsState().value,
            isPhoneInput = false,
            placeholder = stringResource(R.string.enter_birth_placeholder),
            onFocus = fun(focusState: Boolean) {
                viewModel.onEvent(RegEvent.OnBirthFieldFocused(focusState))
            },
            onValueChange = fun(birth: String) {
                viewModel.onEvent(RegEvent.UpdateBirth(birth))
            },
            error = birthFieldError.value
        )
        if (viewModel.birthFieldFocusState.collectAsState().value && birthFieldError.value.isEmpty()) {
            HintText(text = "Введите дату рождения в формате дд/мм/гггг")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EmailTextField(viewModel: RegViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = BringIntoViewRequester()

    val emailFieldError = remember {
        mutableStateOf("")
    }
    if (viewModel.emailFieldState.collectAsState().value is Util.TextFieldState.Failure) {
        emailFieldError.value =
            (viewModel.emailFieldState.collectAsState().value as Util.TextFieldState.Failure).error
    }
    ProperTextField(
        modifier = Modifier
            .padding(
                8.dp
            )
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.auth_field_height).value.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_email_24),
                contentDescription = "account_icon"
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        valueText = viewModel.email.collectAsState().value,
        isPhoneInput = false,
        placeholder = stringResource(R.string.enter_email_placeholder),
        onFocus = fun(focusState: Boolean) {
//            coroutineScope.launch {
//                delay(250)
//                bringIntoViewRequester.bringIntoView()
//            }
            viewModel.onEvent(RegEvent.OnEmailFieldFocused(focusState))
        },
        onValueChange = fun(email: String) {
            viewModel.onEvent(RegEvent.UpdateEmail(email))
        },
        error = emailFieldError.value,
        onDone = {

        }
    )
}