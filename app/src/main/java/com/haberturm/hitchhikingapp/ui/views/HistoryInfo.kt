package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haberturm.hitchhikingapp.R

@Composable
fun HistoryInfo(
    showHistory: Boolean,
    updateDropDownState: () -> Unit,
) {
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
                text = "История поездок",
                fontSize = 20.sp
            )
            IconButton(onClick = { updateDropDownState() }) {
                if(showHistory){
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_up_24), contentDescription = "show less")
                }else{
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24), contentDescription = "show more")

                }
            }
        }
    }
}

@Composable
@Preview
fun HistoryInfoPrev() {
    HistoryInfo(
        false,
        {}
    )
}