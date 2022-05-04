package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileInfoItem(
    name: String,
    surname: String,
    image: String,
    phoneNumber: String,
) {
    Card(
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .width(120.dp)
                    .height(150.dp)
                ,
                backgroundColor = Color.LightGray
            ) {

            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = "$surname $name",
                    fontSize = 36.sp,
                    )
                Text(
                    text = phoneNumber,
                    color = Color.LightGray,
                    fontSize = 24.sp,
                )

            }
        }
    }
}

@Composable
@Preview
fun ItemPrev() {
    ProfileInfoItem(
        name = "Ivan",
        surname = "Ivanov",
        phoneNumber = "+7963290909",
        image = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/sm/1-a-bomb.jpg",
    )
}