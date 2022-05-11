package com.haberturm.hitchhikingapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_PAUSE_SERVICE
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_SHOW_PROPER_MAP_STATE
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.haberturm.hitchhikingapp.ui.util.Constants.ACTION_STOP_SERVICE
import com.haberturm.hitchhikingapp.ui.util.Constants.NOTIFICATION_CHANNEL_ID
import com.haberturm.hitchhikingapp.ui.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.haberturm.hitchhikingapp.ui.util.Constants.NOTIFICATION_ID


class LocationService : LifecycleService() {
    private val TAG = "LOCATION_SERVICE"

    private var isFirstRun = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun) {
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