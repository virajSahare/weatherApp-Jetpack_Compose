package com.check_weather.presentation.notification

import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.provider.Settings
import androidx.compose.ui.graphics.toArgb
import com.check_weather.weatherapp.ui.theme.CardColor

fun weatherNotificationChannel(
    context: Context
) {
    //creating notification channel for the notification
    val channelId = "Weather_channel"
    val channelName = "Weather Updates"
    val channelDescription = "Notification for the weather updates"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = android.app.NotificationChannel(channelId, channelName, importance).apply {
        description = channelDescription
        enableLights(true)
        lightColor = CardColor.toArgb()
        enableVibration(true)
        setSound(
            Settings.System.DEFAULT_NOTIFICATION_URI,
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
        )
    }

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)

}