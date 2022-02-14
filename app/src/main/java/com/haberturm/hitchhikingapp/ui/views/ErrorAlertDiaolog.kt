package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haberturm.hitchhikingapp.R

@Composable
fun ErrorAlertDialog(
    title: String,
    text: String,
    button1Text: String,
    button2Text: String,
    buttonFunction: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Column() {
                    Text(text = text)
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            buttonFunction()
                        }
                    ) {
                        Text(text = button1Text)
                    }
                    Button(
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text(text = button2Text)
                    }
                }
            }
        )
    }

}

@Preview
@Composable
fun ErrorAlertDialogPreview() {
    ErrorAlertDialog(
        title = stringResource(R.string.LocationRationaleTitle),
        text = stringResource(R.string.LocationRationaleText),
        button1Text = stringResource(R.string.LocationRationaleButton1),
        button2Text = stringResource(R.string.LocationRationaleButton2),
        {}
    )
}