package com.haberturm.hitchhikingapp.ui.views

import android.os.Message
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haberturm.hitchhikingapp.R

@Composable
fun MessageItem(){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "Иванов Иван", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Привет",
                        color = Color.Gray,

                        )
                }
            }
            Text(text = "9:28", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(1.dp).width((screenWidth*0.75).dp).background(Color.Gray))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.avatar_w),
                    contentDescription = "avatar2",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "Петрова Анна", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Пока",
                        color = Color.Gray,

                        )
                }
            }
            Text(text = "7:32", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(1.dp).width((screenWidth*0.75).dp).background(Color.Gray))
    }
}

@Composable
@Preview
fun MessageItemPrev(){
    MessageItem()
}