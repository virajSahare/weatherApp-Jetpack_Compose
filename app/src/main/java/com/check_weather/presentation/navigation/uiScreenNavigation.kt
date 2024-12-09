package com.check_weather.presentation.navigation

import android.annotation.SuppressLint
import android.view.Window
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.check_weather.presentation.navconstants.ManageCitiesScreen
import com.check_weather.presentation.navconstants.SpecificWeatherCityScreen
import com.check_weather.presentation.navconstants.SplashScreen
import com.check_weather.presentation.navconstants.WeatherScreen
import com.check_weather.presentation.uiscreens.searchcitiesscreen.ManageCitiesScreen
import com.check_weather.presentation.uiscreens.splashscreen.SplashScreen
import com.check_weather.presentation.uiscreens.weatherscreen.SpecificWeatherCityScreen
import com.check_weather.presentation.uiscreens.weatherscreen.WeatherScreen

@SuppressLint("NewApi")
@Composable
fun UiScreenNavigation(window: Window) {
    //setup the screen navigation logic
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SplashScreen,
        enterTransition = {
            scaleIn(initialScale = 0.8f) + fadeIn() + slideInHorizontally(
                initialOffsetX = { it / 4 }
            )
        }, exitTransition = {
            scaleOut(
                targetScale = 1.2f
            ) + fadeOut() + slideOutHorizontally(targetOffsetX = { -it / 4 })
        }, popEnterTransition = {
            scaleIn(initialScale = 1.2f) + fadeIn() + slideInHorizontally(initialOffsetX = { -it / 4 })
        },
        popExitTransition = {
            scaleOut(targetScale = 0.8f) + fadeOut() + slideOutHorizontally(targetOffsetX = { it / 4 })
        }
    )
    {
        composable<SplashScreen> {
            SplashScreen(navController, window)
        }
        composable<WeatherScreen> {
            WeatherScreen(navController, window)
        }
        composable<ManageCitiesScreen> {
            ManageCitiesScreen(navController, window)
        }
        composable<SpecificWeatherCityScreen> {
            SpecificWeatherCityScreen(navController, window)
        }
    }
}