package com.haberturm.hitchhikingapp.ui.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import com.haberturm.hitchhikingapp.ui.nav.NavRoute

const val KEY_CONTENT_PAGE_INDEX = "CONTENT_PAGE_INDEX"

object HomeRoute : NavRoute<HomeViewModel> {
    override val route: String = "home/{$KEY_CONTENT_PAGE_INDEX}/"

    /**
     * Returns the route that can be used for navigating to this page.
     */
    fun get(index: Int): String = route.replace("{$KEY_CONTENT_PAGE_INDEX}", "$index")

    /*
    not used yet

    fun getIndexFrom(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<Int>(KEY_CONTENT_PAGE_INDEX)

    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(KEY_CONTENT_PAGE_INDEX) { type = NavType.IntType })
    */
    @Composable
    override fun Content(viewModel: HomeViewModel) = Home()

    @Composable
    override fun viewModel(): HomeViewModel = hiltViewModel()

}

@Composable
private fun Home(){
    Text(text = "Home Page")
}