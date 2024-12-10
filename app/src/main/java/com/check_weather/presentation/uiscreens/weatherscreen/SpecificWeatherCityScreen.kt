package com.check_weather.presentation.uiscreens.weatherscreen

import android.content.Context
import android.location.Geocoder
import android.view.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.check_weather.presentation.weatherviewmodel.SpecificCityNameViewModel
import com.check_weather.presentation.weatherviewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


@Composable
fun SpecificWeatherCityScreen(
    navController: NavHostController,
    window: Window,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    finalCityViewModel: SpecificCityNameViewModel = hiltViewModel()
) {
    statusBarColorLogic(window)
    val cityTitle = rememberSaveable { mutableStateOf("City Name") }
    val locationBarVisible = rememberSaveable { mutableStateOf(false) }
    val shouldFetchWeather = rememberSaveable { mutableStateOf(false) }
    val isWeatherUIReady = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    }, modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterVertically)
                        .shadow(
                            elevation = 24.dp,
                            ambientColor = Color(color = 0xFF000000),
                            spotColor = Color(color = 0xffe5e5e5),
                            shape = CircleShape.copy(all = CornerSize(12.dp))
                        )
                        .border(
                            width = 1.5.dp, brush = Brush.linearGradient(
                                listOf(Color(color = 0xFF000000), Color(color = 0xff14213d))
                            ), shape = CircleShape.copy(all = CornerSize(12.dp))
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(color = 0xFF000000)
                    )
                ) {
                    Icon(
                        modifier = Modifier.wrapContentSize(),
                        imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = null
                    )
                }
                Column {
                    Text(
                        text = cityTitle.value,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(5.5.dp))
                    if (locationBarVisible.value) {
                        Image(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            alignment = Alignment.Center
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = Color.Blue,
                            strokeCap = StrokeCap.Round,
                            strokeWidth = 5.dp
                        )
                    }
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                isWeatherUIReady.value = true
                locationBarVisible.value = true
                if (isWeatherUIReady.value) {
                    SpecificCityWeatherDisplayUI(
                        specificCityweatherViewModel = finalCityViewModel,
                        weatherViewModel = weatherViewModel,
                        cityNameDisplay = {
                            cityTitle.value=it
                        }
                    )
                }

            }
        }
    }

}

@Composable
fun SpecificCityWeatherDisplayUI(
    specificCityweatherViewModel: SpecificCityNameViewModel,
    weatherViewModel: WeatherViewModel,
    cityNameDisplay:(String)->Unit
) {
    specificCityweatherViewModel.getSpecificCityName(key = "CityName")
    val specificCityWeatherResult by specificCityweatherViewModel.cityName.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    if (specificCityWeatherResult != null) {
        cityNameDisplay.invoke(specificCityWeatherResult.toString())
        LaunchedEffect(specificCityWeatherResult) {
            // Fetch Weather
            weatherViewModel.fetchWeather(city = specificCityWeatherResult.toString())
            // Fetch AQI by obtaining coordinates
            scope.launch {
                val coordinates = getCoordinatesForCity(context, specificCityWeatherResult.toString())
                if (coordinates != null) {
                    val (latitude, longitude) = coordinates
                    weatherViewModel.fetchAQI(latitude = latitude, longitude = longitude)
                }
            }
        }
        // Display UI
        WeatherDisplayUI(weatherViewModel = weatherViewModel)
    }

}
suspend fun getCoordinatesForCity(context: Context, cityName: String): Pair<Double, Double>? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(cityName, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                Pair(address.latitude, address.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
