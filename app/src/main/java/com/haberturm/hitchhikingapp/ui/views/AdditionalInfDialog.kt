package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haberturm.hitchhikingapp.R


@Composable
fun AdditionalInfDialog(
    carNumberTextValue: String,
    carNumberOnValueChange: (String) -> Unit,
    carInfoTextValue: String,
    carInfoOnValueChange: (String) -> Unit,
    carColorTextValue: String,
    carColorOnValueChange: (String) -> Unit,

    ) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(
                    text = "Заполните дополнитнльную информацию",
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column() {
                    Text(text = "Для поиска попучтиков, заполните информацию о вашем транспортном средстве")
                    //car number
                    ProperTextField(
                        modifier = Modifier
                            .padding(
                                top = 8.dp
                            )
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.auth_field_height).value.dp),
                        onValueChange = carNumberOnValueChange,
                        valueText = carNumberTextValue,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        placeholder = "Введите номер машины"
                    )
                    //car info
                    ProperTextField(
                        modifier = Modifier
                            .padding(
                                top = 8.dp
                            )
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.auth_field_height).value.dp),
                        onValueChange = carInfoOnValueChange,
                        valueText = carInfoTextValue,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        placeholder = "Введите марку машины"
                    )
                    //car color
                    ProperTextField(
                        modifier = Modifier
                            .padding(
                                top = 8.dp
                            )
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.auth_field_height).value.dp),
                        onValueChange = carColorOnValueChange,
                        valueText = carColorTextValue,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        placeholder = "Введите цвет"
                    )
                }
            },
            buttons = {
                OvalButton(
                    onClick = { /*TODO*/ },
                    text = "Отправить",
                    modifier = Modifier
                        .padding(
                            horizontal = 8.dp,
                            vertical = 8.dp
                        )
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.auth_field_height).value.dp)

                )
            }
        )
    }
}

@Composable
@Preview
fun AdditionalInfDialogPrev() {
    AdditionalInfDialog(
        "",
        {},
        "",
        {},
        "",
        {}
    )
}