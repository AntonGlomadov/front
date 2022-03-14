package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LetsGoButton() {
    val configuration = LocalConfiguration.current
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .width(
                configuration.screenWidthDp.dp * 0.75f
            )
            .height(50.dp)
    ) {
        Text(
            text = "Поехали!"
        )
    }
}

@Composable
@Preview
fun LetsGoButtonPrev() {
    LetsGoButton()
}
