package com.check_weather.presentation.uiscreens.splashscreen

import android.view.Window
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavHostController
import com.check_weather.presentation.allcomponents.AllComponents
import com.check_weather.presentation.allcomponents.AllComponents.Companion.SplashScreenlinearProgressIndicator
import com.check_weather.presentation.navconstants.SplashScreen
import com.check_weather.presentation.navconstants.WeatherScreen
import com.check_weather.weatherapp.ui.theme.CardColor
import com.check_weather.weatherapp.ui.theme.Purple40
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (navController: NavHostController, window: Window) {
    val statusBarColor = Brush.linearGradient(
        listOf(CardColor,Purple40),
        start = Offset(0f, 0f), end = Offset(0f, Float.NEGATIVE_INFINITY)
    )
    window.statusBarColor = statusBarColor.hashCode()
    var startAnimation by rememberSaveable { mutableStateOf(false) }
    var isAnimationFunctionCall by rememberSaveable { mutableStateOf(false) }
    var splashScreenDefaultTitle by rememberSaveable { mutableStateOf("") }
    var isProgressIndicatorVisible by rememberSaveable { mutableStateOf(true) }
    var isSplashScreenSearchImageVisible by rememberSaveable { mutableStateOf(false) }
    var isCardVisible by rememberSaveable { mutableStateOf(false) }
    isAnimationFunctionCall = true
    if (isAnimationFunctionCall) {
        SplashScreenAnimation(
            startAnimation, splashScreenDefaultTitle,
            isProgressIndicatorVisible,
            isSplashScreenSearchImageVisible,
            isCardVisible
        )
    }
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        isProgressIndicatorVisible = false
        isCardVisible = true
        splashScreenDefaultTitle = "Let's Check Weather"
        isSplashScreenSearchImageVisible = true
        delay(2000).hashCode()
        navController.navigate(WeatherScreen) {
            popUpTo<SplashScreen> {
                inclusive = true
            }
        }
    }
}

@Composable
fun SplashScreenAnimation(
    startAnimation: Boolean,
    splashScreenDefaultTitle: String,
    isProgressIndicatorVisible: Boolean,
    isSplashScreenSearchImageVisible: Boolean,
    isCardVisible: Boolean
) {
    val animationAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f, label = "animation",
        animationSpec = tween(durationMillis = 3000)
    )
    AllComponents.SplashScreenBackground(animationAlpha.value)
    if (isProgressIndicatorVisible) {
        SplashScreenlinearProgressIndicator()
    }
    SplashScreenSearchIcon(
        splashScreenDefaultTitle, isSplashScreenSearchImageVisible,
        isCardVisible
    )
}

@Composable
fun SplashScreenSearchIcon(
    splashScreenDefaultTitle: String,
    isSplashScreenSearchImageVisible: Boolean,
    isCardVisible: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1000), RepeatMode.Reverse
        ), label = "scale"
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                },
        ) {
            AllComponents.SplashScreenText(
                splashScreenDefaultTitle,
                isSplashScreenSearchImageVisible,
                isCardVisible
            )
        }
    }
}
