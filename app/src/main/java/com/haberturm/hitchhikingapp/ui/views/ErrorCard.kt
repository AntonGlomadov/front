package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haberturm.hitchhikingapp.ui.theme.ErrorColor

@Composable
fun ErrorCard(errorText: String){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 32.dp,
        backgroundColor = ErrorColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            Text(
                text = errorText,
                color = Color.White
            )
        }
    }
}

@Composable
@Preview
fun ErrorCardPrev(){
    ErrorCard("Неверный формат номера")
}