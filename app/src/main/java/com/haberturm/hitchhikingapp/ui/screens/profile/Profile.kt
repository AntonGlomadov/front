package com.haberturm.hitchhikingapp.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.nav.getOrThrow
import com.haberturm.hitchhikingapp.ui.screens.home.HomeRoute
import com.haberturm.hitchhikingapp.ui.util.Constants
import com.haberturm.hitchhikingapp.ui.views.*

const val MODE = "MODE"

object ProfileRoute : NavRoute<ProfileViewModel> {

    override val route = "profile/{$MODE}/"

    fun get(mode: String): String = route.replace("{$MODE}", mode)

    fun getArgFrom(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<String>(MODE)

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(MODE) { type = NavType.StringType })

    @Composable
    override fun viewModel(): ProfileViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: ProfileViewModel) = Profile(viewModel)
}

@Composable
private fun Profile(
    viewModel: ProfileViewModel
) {
    Log.i("MYSTATE", "${viewModel.navigationState.collectAsState().value}")
    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.padding(8.dp)) {
            ProfileInfoItem(
                name = "Ivan",
                surname = "Ivanov",
                phoneNumber = "+7963290909",
                image = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/sm/1-a-bomb.jpg",
            )
            Spacer(modifier = Modifier.height(8.dp))
            ModeInfoPicker(
                mode = when (viewModel.userMode.collectAsState().value) {
                    is Constants.UserMode.Companion -> {
                        ModeUiPresentation.COMPANION.mode
                    }
                    is Constants.UserMode.Driver -> {
                        ModeUiPresentation.DRIVER.mode
                    }
                    else -> {
                        ""
                    } //impossible case
                },
                checked = viewModel.modeSwitchState.collectAsState().value,
                onCheckedChange = { viewModel.onEvent(ProfileEvent.UpdateModeSwitchState) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HistoryInfo(
                showHistory = viewModel.dropDownState.collectAsState().value,
                updateDropDownState = { viewModel.onEvent(ProfileEvent.UpdateDropDownState) }
            )
        }
        val userMode = viewModel.userMode.collectAsState().value
        BottomNavBar(
            navigateToMap = {
                viewModel.onEvent(
                    ProfileEvent.NavigateTo(
                        route = HomeRoute.get(
                            when(userMode){
                                is Constants.UserMode.Companion -> { Constants.NavArgConst.COMPANION.arg }
                                is Constants.UserMode.Driver -> {Constants.NavArgConst.DRIVER.arg}
                                else -> {""} //impossible, i hope ;)
                            }
                        )
                    )
                )
            },
            navigateToProfile = {},
            currentItem = Items.ACCOUNT
        )
    }
}

enum class ModeUiPresentation(val mode: String) {
    DRIVER("Водитель"),
    COMPANION("Попутчик")
}