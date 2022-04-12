package com.haberturm.hitchhikingapp.ui.views


import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.auth.TAG


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProperTextField(
    leadingIcon: (@Composable() () -> Unit)? = null,
    modifier: Modifier,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onDone: (String) -> Unit = { },
    onFocus: (Boolean) -> Unit = { },
    onValueChange: (String) -> Unit = { },
    valueText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
    visualTransformation: VisualTransformation? = null,
    isPhoneInput: Boolean = false,
    placeholder: String = "",

) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf<String>(valueText) }
    LaunchedEffect(key1 = valueText, block = {
        text = valueText
    })

    val clearButtonVisible = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = text, block = {
        clearButtonVisible.value = text != ""
    })
    Column(modifier = Modifier
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }) {
        TextField(
            value = text,
            onValueChange = { txt ->
                if (isPhoneInput) {
                    var check = true
                    txt.forEach {
                        if (!it.isDigit() && it != '+') {
                            check = false
                        }
                    }
                    if (check) {
                        text = txt
                    }
                } else {
                    text = txt
                }
                onValueChange(text)
            },
            modifier = modifier.onFocusEvent {
                if (it.isFocused) {
                    onFocus(true)
                } else if (!it.isFocused) {
                    onFocus(false)
                }
            },
            shape = RoundedCornerShape(32.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (clearButtonVisible.value) {
                    IconButton(
                        onClick = { text = "" },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_clear_24),
                            contentDescription = "clear_text",
                        )
                    }
                }

            },
            interactionSource = interactionSource,
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onDone(text)
            }),
            keyboardOptions = keyboardOptions,
            placeholder = {
                Text(text = placeholder)
            },
            visualTransformation = visualTransformation ?: VisualTransformation.None,
        )
    }

}

