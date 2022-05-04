package com.haberturm.hitchhikingapp.ui.screens.home.map

//I WILL USE IT LATER



//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.pm.PackageManager
//import android.location.Location
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.model.LatLng
//import com.haberturm.hitchhikingapp.MainActivity
//
//
//class UserLocation(private val activity: MainActivity) {
//    // The entry point to the Fused Location Provider.
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//
//    // A default location (Sydney, Australia) and default zoom to use when location permission is
//    // not granted.
//    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
//    private var locationPermissionGranted = false
//
//    // The geographical location where the device is currently located. That is, the last-known
//    // location retrieved by the Fused Location Provider.
//    private var lastKnownLocation: Location? = null
//
//    @SuppressLint("MissingPermission")
//    private fun getDeviceLocation(): Location? {
//
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        var location: Location? = null
//        try {
//            if (locationPermissionGranted) {
//                val locationResult = fusedLocationProviderClient.lastLocation
//                locationResult.addOnCompleteListener() { task ->
//                    if (task.isSuccessful) {
//                        // Set the map's camera position to the current location of the device.
//                        lastKnownLocation = task.result
//                        if (lastKnownLocation != null) {
//                           location = lastKnownLocation
////                            map?.moveCamera(
////                                CameraUpdateFactory.newLatLngZoom(
////                                LatLng(lastKnownLocation!!.latitude,
////                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
//                        }
//                    } else {
//                        Log.d(TAG, "Current location is null. Using defaults.")
//                        Log.e(TAG, "Exception: %s", task.exception)
////                        map?.moveCamera(
////                            CameraUpdateFactory
////                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
////                        map?.uiSettings?.isMyLocationButtonEnabled = false
//                    }
//                }
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message, e)
//        }
//        return location
//    }
//    // [END maps_current_place_get_device_location]
//
//    /**
//     * Prompts the user for permission to use the device location.
//     */
//    // [START maps_current_place_location_permission]
//    private fun getLocationPermission() {
//        /*
//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.
//         */
//        if (ContextCompat.checkSelfPermission(activity.applicationContext,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED) {
//            locationPermissionGranted = true
//        } else {
//            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
//        }
//    }
//    // [END maps_current_place_location_permission]
//
//    /**
//     * Handles the result of the request for location permissions.
//     */
//    // [START maps_current_place_on_request_permissions_result]
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>,
//                                            grantResults: IntArray) {
//        locationPermissionGranted = false
//        when (requestCode) {
//            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
//
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.isNotEmpty() &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    locationPermissionGranted = true
//                }
//            }
//        }
//       // updateLocationUI()
//    }
//
//    companion object {
//        private val TAG = "MapsActivityCurrentPlace::class.java.simpleName"
//        private const val DEFAULT_ZOOM = 15
//        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//
//        // Keys for storing activity state.
//        // [START maps_current_place_state_keys]
//        private const val KEY_CAMERA_POSITION = "camera_position"
//        private const val KEY_LOCATION = "location"
//        // [END maps_current_place_state_keys]
//
//        // Used for selecting the current place.
//        private const val M_MAX_ENTRIES = 5
//    }
//
//}