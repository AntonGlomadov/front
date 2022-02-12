package com.haberturm.hitchhikingapp.ui.home.map

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

sealed class MyPermissionState{
    object HasPermission: MyPermissionState()
    object ShouldShowRationale : MyPermissionState()
    object PermanentlyDenied : MyPermissionState()

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetPermissions(): MultiplePermissionsState {
    return  rememberMultiplePermissionsState(
        permissions = listOf(
            //Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDenied():  Boolean {
    return !shouldShowRationale && !hasPermission
}
@OptIn(ExperimentalPermissionsApi::class)
fun  MultiplePermissionsState.checkPermissions(): MyPermissionState{ // fix it for multiple permission
    var locationPermissionState: MyPermissionState? = null

    permissions.forEach { perm ->
        when(perm.permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                when {
                    perm.hasPermission -> {
                        Log.i("perm", "Location permission accepted")
                        locationPermissionState = MyPermissionState.HasPermission

                    }
                    perm.shouldShowRationale -> {
                        Log.i("perm",  "Location permission is needed" +
                                "to access the map")
                        locationPermissionState = MyPermissionState.ShouldShowRationale
                    }
                    perm.isPermanentlyDenied() -> {
                        Log.i("perm",  "Location permission was permanently" +
                                "denied. You can enable it in the app" +
                                "settings.")
                        locationPermissionState = MyPermissionState.PermanentlyDenied
                    }
                }
            }
        }
    }
    return locationPermissionState!!
}