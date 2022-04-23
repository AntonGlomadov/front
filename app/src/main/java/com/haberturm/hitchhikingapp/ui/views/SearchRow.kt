package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.haberturm.hitchhikingapp.R


@Composable
fun SearchRow(
    iconId: Int,
    value: String = "",
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        OutlinedButton(onClick = { /*TODO*/ },
            modifier= Modifier
                .size(60.dp)
                .padding(0.dp),  //avoid the oval shape
            shape = CircleShape,
            border= BorderStroke(1.dp, Color.Blue),
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Blue),
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "back_button",
                Modifier
                    .rotate(90f)


            )
        }

        ProperTextField(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Blue), RoundedCornerShape(32.dp))
                .height(60.dp)
            ,
            valueText = value,
        )

    }
}


@Preview
@Composable
fun SearchRowPrev(){
    SearchRow(iconId = R.drawable.ic_baseline_navigation_24,"")
}