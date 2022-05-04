package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haberturm.hitchhikingapp.R

@Composable
fun SelectModeDialog(
    changeUserModeToCompanion: () -> Unit,
    changeUserModeToDriver: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            title = {
                Text(text = stringResource(R.string.select_mode_dialog_header))
            },
            buttons = {
                Column(Modifier.padding(32.dp)) {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(text = stringResource(R.string.select_driver_text))
                        },
                        onClick = {
                            changeUserModeToDriver()
                            openDialog.value = false
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_car_24),
                                contentDescription = "driver_icon"
                            )
                        },
                        backgroundColor = MaterialTheme.colors.background,
                        modifier = Modifier.border(
                            shape = RoundedCornerShape(32.dp),
                            color = MaterialTheme.colors.primary,
                            width = 1.dp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExtendedFloatingActionButton(
                        text = {
                            Text(text = stringResource(R.string.select_companion_text))
                        },
                        onClick = {
                            changeUserModeToCompanion()
                            openDialog.value = false
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_people_24),
                                contentDescription = "companion_icon"
                            )
                        },
                        backgroundColor = MaterialTheme.colors.background,
                        modifier = Modifier.border(
                            shape = RoundedCornerShape(32.dp),
                            color = MaterialTheme.colors.primary,
                            width = 1.dp
                        )
                    )
                }
            }
        )
    }
}

@Composable
@Preview
fun SelectModeDialogPrev() {
    SelectModeDialog({}, {})
}