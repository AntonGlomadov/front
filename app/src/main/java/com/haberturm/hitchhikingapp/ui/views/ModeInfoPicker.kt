package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModeInfoPicker(
    mode: String
){
    val checked = remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Текущий режим: $mode",
                fontSize = 20.sp
            )
            Switch(checked = checked.value, onCheckedChange = {checked.value = !checked.value})
        }
    }
}

@Composable
@Preview
fun ModeInfoPickerPrev(){
    ModeInfoPicker(mode = "Водитель")
}