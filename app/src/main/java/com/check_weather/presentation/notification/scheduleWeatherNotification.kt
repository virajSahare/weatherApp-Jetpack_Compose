package com.check_weather.presentation.notification

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

//creating the function to schedule the notification after 2 days using workManager
 fun scheduleWeatherNotification(
    context: Context
) {
    val workRequest = PeriodicWorkRequestBuilder<WeatherNotificationWorker>(1, TimeUnit.DAYS) // Check daily
        .build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "AppUsageCheck",
        androidx.work.ExistingPeriodicWorkPolicy.KEEP, // Prevents duplicate work
        workRequest
    )
}