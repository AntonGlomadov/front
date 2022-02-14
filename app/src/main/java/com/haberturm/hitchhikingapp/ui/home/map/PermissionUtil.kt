package com.haberturm.hitchhikingapp.ui.home.map

import android.Manifest
import android.util.Log

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.haberturm.hitchhikingapp.R
import com.haberturm.hitchhikingapp.ui.views.ErrorAlertDialog

sealed class MyPermissionState {
    object HasPermission : MyPermissionState()
    object ShouldShowRationale : MyPermissionState()
    object PermanentlyDenied : MyPermissionState()

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetPermissions(): MultiplePermissionsState {
    return rememberMultiplePermissionsState(
        permissions = listOf(
            //Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !shouldShowRationale && !hasPermission
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiplePermissionsState.checkPermissions(): MyPermissionState { // fix it for multiple permission
    var locationPermissionState: MyPermissionState? = null
    //TODO if permission denied app will show Kaliningrad. Its better show a permanent err screen
    //TODO alert dialog doesn't survive screen rotation
    permissions.forEach { perm ->
        when (perm.permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                when {
                    perm.hasPermission -> {
                        Log.i("perm", "Location permission accepted")
                        locationPermissionState = MyPermissionState.HasPermission


                    }
                    perm.shouldShowRationale -> {
                        Log.i(
                            "perm", "Location permission is needed" +
                                    "to access the map"
                        )
                        //perm.launchPermissionRequest()
                        ErrorAlertDialog(
                            title = stringResource(R.string.LocationRationaleTitle),
                            text = stringResource(R.string.LocationRationaleText),
                            button1Text = stringResource(R.string.LocationRationaleButton1),
                            button2Text = stringResource(R.string.LocationRationaleButton2),
                            { perm.launchPermissionRequest() }
                        )
                        locationPermissionState = MyPermissionState.ShouldShowRationale

                    }
                    perm.isPermanentlyDenied() -> {
                        Log.i(
                            "perm", "Location permission was permanently" +
                                    "denied. You can enable it in the app" +
                                    "settings."
                        )
                        ErrorAlertDialog(
                            title = stringResource(R.string.LocationPermanentTitle),
                            text = stringResource(R.string.LocationRationaleText),
                            button1Text = stringResource(R.string.LocationRationaleButton1),
                            button2Text = stringResource(R.string.LocationRationaleButton2),
                            {} //TODO intent to app settings

                        )
                        locationPermissionState = MyPermissionState.PermanentlyDenied
                    }
                }
            }
        }
    }
    return locationPermissionState!!
}