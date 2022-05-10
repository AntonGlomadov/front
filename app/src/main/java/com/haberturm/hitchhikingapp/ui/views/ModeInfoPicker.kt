package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haberturm.hitchhikingapp.R

@Composable
fun ModeInfoPicker(
    mode: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                Text(
                    text = "Текущий режим: ",
                    fontSize = 20.sp
                )
                Text(
                    text = mode,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }
            Row(
                Modifier.padding(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_people_24),
                    contentDescription = "companion mode",
                    tint = if (checked) {
                        Color.LightGray
                    } else {
                        MaterialTheme.colors.primary
                    }
                )
                Switch(
                    checked = checked,
                    onCheckedChange = { onCheckedChange() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.surface,
                        checkedTrackColor = MaterialTheme.colors.onSurface,
                        checkedTrackAlpha = 0.38f,
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_car_24),
                    contentDescription = "driver mode",
                    tint = if (checked) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.LightGray
                    }
                )
            }

        }
    }
}

@Composable
@Preview
fun ModeInfoPickerPrev() {
    ModeInfoPicker(
        mode = "Водитель",
        false,
        {}
    )
}