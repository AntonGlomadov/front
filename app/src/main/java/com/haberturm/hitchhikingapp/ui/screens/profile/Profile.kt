package com.haberturm.hitchhikingapp.ui.screens.profile

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
import com.haberturm.hitchhikingapp.ui.screens.auth.password.Password
import com.haberturm.hitchhikingapp.ui.screens.auth.password.PasswordViewModel
import com.haberturm.hitchhikingapp.ui.screens.home.UserMode
import com.haberturm.hitchhikingapp.ui.views.HistoryInfo
import com.haberturm.hitchhikingapp.ui.views.ModeInfoPicker
import com.haberturm.hitchhikingapp.ui.views.ProfileInfoItem

const val PHONE_NUMBER = "PHONE_NUMBER"

object ProfileRoute : NavRoute<ProfileViewModel> {

    override val route = "password/{$PHONE_NUMBER}/"

    fun get(number: String): String = route.replace("{$PHONE_NUMBER}", number)

    fun getArgFrom(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<String>(PHONE_NUMBER)

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(PHONE_NUMBER) { type = NavType.StringType })

    @Composable
    override fun viewModel(): ProfileViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: ProfileViewModel) = Profile(viewModel)
}

@Composable
private fun Profile(
    viewModel: ProfileViewModel
){
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ProfileInfoItem(
            name = "Ivan",
            surname = "Ivanov",
            phoneNumber = "+7963290909",
            image = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/images/sm/1-a-bomb.jpg",
        )
        Spacer(modifier = Modifier.height(8.dp))
        ModeInfoPicker(
            mode = when(viewModel.userMode.collectAsState().value){
                is UserMode.Companion -> {ModeUiPresentation.COMPANION.mode}
                is UserMode.Driver -> {ModeUiPresentation.DRIVER.mode}
                else -> {""} //impossible case
            },
            checked = viewModel.modeSwitchState.collectAsState().value,
            onCheckedChange = {viewModel.onEvent(ProfileEvent.UpdateModeSwitchState)}
        )
        Spacer(modifier = Modifier.height(8.dp))
        HistoryInfo(
            showHistory = viewModel.dropDownState.collectAsState().value,
            updateDropDownState = {viewModel.onEvent(ProfileEvent.UpdateDropDownState)}
        )
    }
}

enum class ModeUiPresentation(val mode:String){
    DRIVER("Водитель"),
    COMPANION("Попутчик")
}