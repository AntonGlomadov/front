package com.haberturm.hitchhikingapp.ui.screens.home

import android.Manifest
import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.screens.home.map.GetPermissions
import com.haberturm.hitchhikingapp.ui.screens.home.map.isPermanentlyDenied
import com.haberturm.hitchhikingapp.ui.nav.NavRoute
import com.haberturm.hitchhikingapp.ui.views.ErrorAlertDialog
import com.haberturm.hitchhikingapp.ui.screens.home.map.*
import com.haberturm.hitchhikingapp.ui.nav.NavConst
import com.haberturm.hitchhikingapp.ui.nav.getOrThrow
import com.haberturm.hitchhikingapp.ui.screens.profile.ProfileRoute
import com.haberturm.hitchhikingapp.ui.util.Constants
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.haberturm.hitchhikingapp.ui.views.BottomNavBar
import com.haberturm.hitchhikingapp.ui.views.Items
import com.haberturm.hitchhikingapp.ui.views.SelectModeDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


const val MODE = "MODE"

object HomeRoute : NavRoute<HomeViewModel> {
    override val route: String = NavConst.HOME + "{$MODE}/"

    fun get(mode: String): String = route.replace("{$MODE}", mode)

    fun getArgFrom(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<String>(MODE)


    override fun getArguments(): List<NamedNavArgument> = listOf(
        navArgument(MODE) {
            type = NavType.StringType
            defaultValue = Constants.NavArgConst.UNDEFINED.arg
        })

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
    val context = LocalContext.current
    val userMode = viewModel.currentUserMode.collectAsState().value
    if (viewModel.currentUserMode.collectAsState().value is Constants.UserMode.Undefined) {
        SelectModeDialog(
            changeUserModeToCompanion = { viewModel.onEvent(HomeEvent.ChangeUserMode(Constants.UserMode.Companion)) },
            changeUserModeToDriver = { viewModel.onEvent(HomeEvent.ChangeUserMode(Constants.UserMode.Driver)) },
        )
    }
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
    Log.i("DEBUG_LOAD", "${viewModel.userLocationStatus.collectAsState().value}")
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
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = dimensionResource(id = R.dimen.bottom_bar_size))
    ) {
        permissions.permissions.forEach { perm ->
            when (perm.permission) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    when {
                        perm.hasPermission -> {
                            viewModel.launchLocationService(context, ACTION_START_OR_RESUME_SERVICE)
                            Log.i("PERM_DEBUG", "has permission")
                            permissionStatus = PermissionStatus.PermissionAccepted
                            viewModel.getUserLocation(LocalContext.current)
                            Log.i("PERM_DEBUG", "${viewModel.userLocationStatus.collectAsState().value.isLocationReady}")
                            if (viewModel.userLocationStatus.collectAsState().value.isLocationReady) {
                                Log.i("PERM_DEBUG", "location ready")

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
                                androidx.compose.animation.AnimatedVisibility(
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

    BottomNavBar(
        navigateToProfile = {
            viewModel.onEvent(
                HomeEvent.NavigateTo(
                    ProfileRoute.get(
                        when(userMode){
                            is Constants.UserMode.Companion -> { Constants.NavArgConst.COMPANION.arg }
                            is Constants.UserMode.Driver -> {Constants.NavArgConst.DRIVER.arg}
                            else -> {""} //impossible, i hope ;)
                        }
                    )
                )
            )
        },
        navigateToMap = {},
        currentItem = Items.MAP
    )
}





