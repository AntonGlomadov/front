package com.haberturm.hitchhikingapp.ui.screens.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import com.haberturm.hitchhikingapp.ui.screens.profile.ProfileRoute
import com.haberturm.hitchhikingapp.ui.util.Constants
import com.haberturm.hitchhikingapp.ui.views.BottomNavBar
import com.haberturm.hitchhikingapp.ui.views.Items
import com.haberturm.hitchhikingapp.ui.views.MessageItem

const val MODE = "MODE"
object MessageRoute : NavRoute<MessageViewModel> {

    override val route = "message/{${MODE}}/"

    fun get(mode: String): String = route.replace("{$MODE}", mode)

    fun getArgFrom(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<String>(MODE)

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(MODE) { type = NavType.StringType })

    @Composable
    override fun viewModel(): MessageViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: MessageViewModel) = Message(viewModel)
}

@Composable
private fun Message(
    viewModel: MessageViewModel
){
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MessageItem()
        BottomNavBar(
            navigateToMap = {
                viewModel.onEvent(
                    MessageEvent.NavigateTo(
                        route = HomeRoute.get(
                            when (viewModel.userMode) {
                                is Constants.UserMode.Companion -> {
                                    Constants.NavArgConst.COMPANION.arg
                                }
                                is Constants.UserMode.Driver -> {
                                    Constants.NavArgConst.DRIVER.arg
                                }
                                else -> {
                                    ""
                                } //impossible, i hope ;)
                            }
                        )
                    )
                )
            },
            navigateToProfile = {
                viewModel.onEvent(
                    MessageEvent.NavigateTo(
                        ProfileRoute.get(
                            when (viewModel.userMode) {
                                is Constants.UserMode.Companion -> {
                                    Constants.NavArgConst.COMPANION.arg
                                }
                                is Constants.UserMode.Driver -> {
                                    Constants.NavArgConst.DRIVER.arg
                                }
                                else -> {
                                    ""
                                } //impossible, i hope ;)
                            }
                        )
                    )
                )
            },
            navigateToMessage = {},
            currentItem = Items.MESSAGE,
        )
    }
}
