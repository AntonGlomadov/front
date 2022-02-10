package com.haberturm.hitchhikingapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.haberturm.hitchhikingapp.ui.home.map.MapView
import com.haberturm.hitchhikingapp.ui.home.map.rememberMapViewWithLifecycle

import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
private fun Home() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MapView(latitude = 54.6944, longitude = 20.4981)
    }
}