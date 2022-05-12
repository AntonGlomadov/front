package com.haberturm.hitchhikingapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_PAUSE_SERVICE
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_SHOW_PROPER_MAP_STATE
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_STOP_SERVICE
import com.haberturm.hitchhikingapp.ui.util.Constants.FASTEST_LOCATION_INTERVAL
import com.haberturm.hitchhikingapp.ui.util.Constants.LOCATION_UPDATE_INTERVAL
import com.haberturm.hitchhikingapp.ui.util.Constants.NOTIFICATION_CHANNEL_ID
import com.haberturm.hitchhikingapp.ui.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.haberturm.hitchhikingapp.ui.util.Constants.NOTIFICATION_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class LocationService : LifecycleService() {
    private val TAG = "LOCATION_SERVICE"

    private var isFirstRun = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private val _location = MutableStateFlow<LatLng?>(null)
        val locationInService: StateFlow<LatLng?> = _location.asStateFlow()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Log.i(TAG, "Resuming service")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.i(TAG, "Paused service")
                }
                ACTION_STOP_SERVICE -> {
                    Log.i(TAG, "Stopped service")
                }
                else -> Unit
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        startLocationTracking()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationTracking()
    }

    private fun stopLocationTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {
        val request = LocationRequest().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = FASTEST_LOCATION_INTERVAL
            priority = PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.locations?.let { locations ->
                for (location in locations) {
                    _location.value = LatLng(location.latitude, location.longitude)
                    Log.i(TAG, "${location.latitude}, ${location.longitude}")
                }
            }

        }
    }


    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager


        createNotificationChannel(notificationManager)


        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_car_24)
            .setContentTitle("hitch-hiking App")
            .setContentText("tracking location")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_PROPER_MAP_STATE
        },
        FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}