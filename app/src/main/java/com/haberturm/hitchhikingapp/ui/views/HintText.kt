package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HintText(
    text: String
){
    Text(
        text = text,
        color = Color.LightGray,
        textAlign = TextAlign.Center
    )
}