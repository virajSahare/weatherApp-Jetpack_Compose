package com.check_weather.presentation.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.check_weather.presentation.navigation.UiScreenNavigation
import com.check_weather.presentation.notification.weatherNotificationChannel
import com.check_weather.presentation.notification.scheduleWeatherNotification
import com.check_weather.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherNotificationChannel(context = this)
        scheduleWeatherNotification(context = this)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UiScreenNavigation(this.window)
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        // Save the current time as the last open time
        val sharedPreferences = getSharedPreferences("AppUsagePrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("last_open_time", System.currentTimeMillis()).apply()
    }
}