package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OvalButton(
    onClick: () -> Unit,
    text: String = "Поехали!",
    /**float size, % of screen size (ex: 0.5f - half screen width)*/
    modifier: Modifier


) {

    OutlinedButton(
        modifier = modifier,
        onClick = { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary)

    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun LetsGoButtonPrev() {
    val configuration = LocalConfiguration.current
    OvalButton(
        {},
        modifier = Modifier.width(
            configuration.screenWidthDp.dp * 0.5f
        )
            .height(50.dp),
    )
}
