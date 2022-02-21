package com.haberturm.hitchhikingapp.ui.home

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.home.map.GetPermissions
import com.haberturm.hitchhikingapp.ui.home.map.isPermanentlyDenied
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.views.ErrorAlertDialog
import com.haberturm.hitchhikingapp.ui.home.map.*


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


@OptIn(
    ExperimentalPermissionsApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)
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
                if (event == Lifecycle.Event.ON_START) {
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
    //val userLocationPermissionState = permissions.checkPermissions()
    var locationPermissionGranted by remember { mutableStateOf(false) }
    permissions.permissions.forEach { perm ->
        when (perm.permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                when {
                    perm.hasPermission -> {
                        var isMapLoaded by remember { mutableStateOf(false) }
                        viewModel.getUserLocation(LocalContext.current)

                        Box(Modifier.fillMaxSize()) {
                            GoogleMapView(
                                viewModel.latitude,
                                viewModel.longitude,
                                locationPermissionGranted,
                                modifier = Modifier.matchParentSize(),
                                onMapLoaded = {
                                    isMapLoaded = true
                                },

                                )
                            if (!isMapLoaded) {
                                AnimatedVisibility(
                                    modifier = Modifier
                                        .matchParentSize(),
                                    visible = !isMapLoaded,
                                    enter = EnterTransition.None,
                                    exit = fadeOut()
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .background(MaterialTheme.colors.background)
                                            .wrapContentSize()
                                    )
                                }
                            }
                        }
                        locationPermissionGranted = true


                    }
                    perm.shouldShowRationale -> {
                        ErrorAlertDialog(
                            title = stringResource(R.string.LocationRationaleTitle),
                            text = stringResource(R.string.LocationRationaleText),
                            button1Text = stringResource(R.string.LocationRationaleButton1),
                            button2Text = stringResource(R.string.LocationRationaleButton2),
                            { perm.launchPermissionRequest() }
                        )
                        locationPermissionGranted = false

                    }
                    perm.isPermanentlyDenied() -> {
//                        Box(Modifier.fillMaxSize()) {
//                            Text(text = "Location permission denied")
//                        }
                        //TODO think about what to show when permanent denied
                        //TODO !CRITICAL! "permanent denied" dialog shows before permission ask
                        ErrorAlertDialog(
                            title = stringResource(R.string.LocationPermanentTitle),
                            text = stringResource(R.string.LocationRationaleText),
                            button1Text = stringResource(R.string.LocationRationaleButton1),
                            button2Text = "",//stringResource(R.string.LocationRationaleButton2),
                            {}

                        )
                        locationPermissionGranted = false
                    }
                }
            }
        }
    }

//    if (userLocationPermissionState == MyPermissionState.HasPermission) {
//        viewModel.getUserLocation(LocalContext.current)
//        locationPermissionGranted = true
//
//    } else {
//        viewModel.getUserLocation(LocalContext.current)
//
//        //TODO add proper error handler
//    }


}



