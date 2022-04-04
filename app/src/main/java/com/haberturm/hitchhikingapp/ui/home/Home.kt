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
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.home.map.GetPermissions
import com.haberturm.hitchhikingapp.ui.home.map.isPermanentlyDenied
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.views.ErrorAlertDialog
import com.haberturm.hitchhikingapp.ui.home.map.*
import com.haberturm.hitchhikingapp.ui.views.SelectModeDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import user.userdb.UserEntity


const val KEY_HOME_INDEX = "HOME_PAGE_INDEX"

object HomeRoute : NavRoute<HomeViewModel> {
    override val route: String = "home/{$KEY_HOME_INDEX}/"

    /**
     * Returns the route that can be used for navigating to this page.
     */
    fun get(index: Int): String = route.replace("{$KEY_HOME_INDEX}", "$index")

//    fun getIndexFrom(savedStateHandle: SavedStateHandle) =
//        savedStateHandle.getOrThrow<Int>(KEY_HOME_INDEX)

    /*
    not used yet



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
    SelectModeDialog(
        changeUserModeToCompanion = {viewModel.onEvent(HomeEvent.ChangeUserMode(UserMode.Companion))},
        changeUserModeToDriver = {viewModel.onEvent(HomeEvent.ChangeUserMode(UserMode.Driver))},
    )
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

    var isMapAndLocLoaded by remember { mutableStateOf(false) }
    var isLocReady by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        viewModel.userLocationStatus.onEach { event ->
            if (event.isLocationReady) {
                isLocReady = true
            }
            if (event.isLocationReady && event.isMapReady) {
                isMapAndLocLoaded = true
            }
        }.launchIn(this)
    }
    var permissionStatus by remember { mutableStateOf<PermissionStatus>(PermissionStatus.PermissionNotProcessed) }

    when (permissionStatus) {
        is PermissionStatus.PermissionNotProcessed -> {
            Log.i("permissionstatus", "PermissionNotProcessed")
            Text(text = "PermissionNotProcessed")
        }
        is PermissionStatus.PermissionAccepted -> {
            Log.i("permissionstatus", "PermissionAccepted")
            Text(text = "PermissionAccepted")
        }
        is PermissionStatus.PermissionDenied -> {
            Log.i("permissionstatus", "PermissionDenied")
            Text(text = "PermissionDenied")
        }
        is PermissionStatus.PermissionPermanentlyDenied -> {
            Log.i("permissionstatus", "PermissionPermanentlyDenied")
            Text(text = "PermissionPermanentlyDenied")
        }
    }


    var locationPermissionGranted by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        permissions.permissions.forEach { perm ->
            when (perm.permission) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    when {
                        perm.hasPermission -> {
                            permissionStatus = PermissionStatus.PermissionAccepted
                            viewModel.getUserLocation(LocalContext.current)

                            if (isLocReady) {
                                val userLocation = viewModel.location.collectAsState().value
                                GoogleMapView(
                                    userLocation.latitude,
                                    userLocation.longitude,
                                    modifier = Modifier.matchParentSize(),
                                    onMapLoaded = {
                                        viewModel.onEvent(HomeEvent.MapReady)
                                    },
                                    viewModel,
                                    LocalContext.current
                                )
                            }

                            if (!isMapAndLocLoaded) {
                                AnimatedVisibility(
                                    modifier = Modifier
                                        .matchParentSize(),
                                    visible = !isMapAndLocLoaded,
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
                            locationPermissionGranted = true


                        }
                        perm.shouldShowRationale -> {
                            permissionStatus = PermissionStatus.PermissionDenied
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
                            //TODO: fix states to handle this(now states works not as expected)
                            if (permissionStatus != PermissionStatus.PermissionNotProcessed) {
                                ErrorAlertDialog(
                                    title = stringResource(R.string.LocationPermanentTitle),
                                    text = stringResource(R.string.LocationRationaleText),
                                    button1Text = stringResource(R.string.LocationRationaleButton1),
                                    button2Text = "",//stringResource(R.string.LocationRationaleButton2),
                                    {}

                                )
                                permissionStatus = PermissionStatus.PermissionDenied
                                locationPermissionGranted = false
                            }
                        }
                    }
                }
            }
        }
    }
}





