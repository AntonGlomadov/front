package com.haberturm.hitchhikingapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.nav.NavigationState
enum class Items{
    MAP, MESSAGE, ACCOUNT
}

@Composable
fun BottomNavBar(
    navigateToMap: () -> Unit,
    navigateToProfile: () -> Unit,
    currentItem: Items
){
    Column(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomCenter)
    ) {
        Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.bottom_bar_size))
                .padding(start = 48.dp, end = 48.dp, top = 2.dp)
            ,
           // verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navigateToMap() }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_map_24),
                        contentDescription = "map_icon",
                        modifier = Modifier
                            .size(32.dp),
                        tint = if(currentItem == Items.MAP) {
                            MaterialTheme.colors.primary
                        }else{
                            Color.LightGray
                        }
                    )
                    Text(
                        text = "Карта",
                        color = if(currentItem == Items.MAP) {
                            MaterialTheme.colors.primary
                        }else{
                            Color.LightGray
                        }
                    )
                }
            }

            IconButton(onClick = { /*TODO*/ }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_message_24),
                        contentDescription = "message_icon",
                        modifier = Modifier
                            .size(32.dp)
                        ,
                        tint = if(currentItem == Items.MESSAGE) {
                            MaterialTheme.colors.primary
                        }else{
                            Color.LightGray
                        }
                    )
                    Text(
                        text = "Сообщения",
                        color = if(currentItem == Items.MESSAGE) {
                            MaterialTheme.colors.primary
                        }else{
                            Color.LightGray
                        }
                    )
                }
            }

            IconButton(onClick = { navigateToProfile() }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_account_circle_24),
                        contentDescription = "account_icon",
                        modifier = Modifier
                            .size(32.dp),
                        tint = if(currentItem == Items.ACCOUNT) {
                            MaterialTheme.colors.primary
                        }else{
                            Color.LightGray
                        }
                    )
                    Text(
                        text = "Профиль",
                        color = if(currentItem == Items.ACCOUNT) {
                            MaterialTheme.colors.primary
                        }else{
                            Color.LightGray
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun BottomNavBarPrev(){
    BottomNavBar({},{}, currentItem = Items.MAP)
}