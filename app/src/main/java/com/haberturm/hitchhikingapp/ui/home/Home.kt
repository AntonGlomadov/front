package com.haberturm.hitchhikingapp.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.ui.home.map.GetPermissions
import com.haberturm.hitchhikingapp.ui.home.map.MapView
import com.haberturm.hitchhikingapp.ui.home.map.MyPermissionState
import com.haberturm.hitchhikingapp.ui.home.map.checkPermissions
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
    override fun Content(viewModel: HomeViewModel) = Home(viewModel)

    @Composable
    override fun viewModel(): HomeViewModel = hiltViewModel()

}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Home(
    viewModel: HomeViewModel
) {
    val permissions = GetPermissions()
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if(event == Lifecycle.Event.ON_START) {
                    permissions.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    //TODO: handle recomposition
    val userLocationPermissionState = permissions.checkPermissions()
    if (userLocationPermissionState == MyPermissionState.HasPermission){
        viewModel.getUserLocation(LocalContext.current)
    }else{
        //TODO add proper error handler
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MapView(viewModel.latitude, viewModel.longitude )
    }
}