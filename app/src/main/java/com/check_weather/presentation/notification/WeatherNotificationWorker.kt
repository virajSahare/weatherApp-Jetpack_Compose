package com.check_weather.presentation.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.check_weather.weatherapp.R

class WeatherNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val sharedPreferences = applicationContext.getSharedPreferences("AppUsagePrefs", Context.MODE_PRIVATE)
        val lastOpenTime = sharedPreferences.getLong("last_open_time", 0)
        val currentTime = System.currentTimeMillis()

        // Check if the app hasn't been used for 7 days
        if (currentTime - lastOpenTime > 7 * 24 * 60 * 60 * 1000) { // 7 days in milliseconds
            showNotification()
        }
        return Result.success()
    }

     private fun showNotification() {
        val channelId = "Weather_channel"

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.app_feature_iccon)
            .setContentTitle("Check Weather")
            .setContentText("Check the weather in your location!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Automatically dismiss when tapped
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(10, notification)
    }
}
