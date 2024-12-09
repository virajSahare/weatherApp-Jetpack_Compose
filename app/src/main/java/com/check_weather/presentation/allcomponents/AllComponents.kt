package com.check_weather.presentation.allcomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.check_weather.data.model.WeatherAQIResponse
import com.check_weather.data.model.WeatherResponse
import com.check_weather.weatherapp.R
import com.check_weather.weatherapp.ui.theme.CardColor
import com.check_weather.weatherapp.ui.theme.Purple40
import com.check_weather.weatherapp.ui.theme.Sunrise_color
import com.check_weather.weatherapp.ui.theme.Sunset_Color
import com.check_weather.weatherapp.ui.theme.temperatureTitle
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AllComponents {
    companion object {
        @Composable
        fun SplashScreenBackground(animationAlpha: Float) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush =
                        linearGradient(
                            listOf(Purple40, CardColor),
                            start = Offset(0f, 0f), end = Offset(0f, Float.POSITIVE_INFINITY)
                        ),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 520.dp
                        )
                    )
                    .alpha(alpha = animationAlpha),
                contentAlignment = Alignment.Center
            ) {
                //val screenWidth=maxWidth
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    painter = painterResource(R.drawable.app_feature_iccon),
                    alpha = animationAlpha,
                    contentDescription = "Splash_Screen_Display_Image"
                )
            }
        }

        @Composable
        fun SplashScreenlinearProgressIndicator() {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = CardColor,
                trackColor = Purple40,
                strokeCap = StrokeCap.Round,
            )
        }

        @Composable
        fun SplashScreenText(
            splashScreenDefaultTitle: String,
            isSplashScreenSearchImageVisible: Boolean,
            isCardVisible: Boolean
        ) {
            if (isCardVisible) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 10.dp, bottom = 100.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(size = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Text(
                            text = splashScreenDefaultTitle,
                            style = TextStyle(
                                textMotion = TextMotion.Animated
                            ),
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            fontWeight = FontWeight.W900,
                            fontSize = 35.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Cursive
                        )
                        if (isSplashScreenSearchImageVisible) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                modifier = Modifier
                                    .size(35.dp),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun TemperatureDisplayTitle(weatherData: WeatherResponse) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = "${weatherData.main.temp}°c",
                textAlign = TextAlign.Center,
                fontSize = 70.sp,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = temperatureTitle
            )
            weatherData.weather[0].main.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    text = it,
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                text = weatherData.weather[0].description,
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif
            )
            val iconUrl =
                "https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png"
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
                    .height(130.dp),
                model = iconUrl,
                contentDescription = null
            )
        }

        @Composable
        fun AQIDataDisplayCard(aqiData: WeatherAQIResponse) {
            val aqiQualityCategories = listOf("Good", "Fair", "Moderate", "Poor", "Very Poor")
            val aqiIndex = aqiData.list.first().main.aqi
            val components = aqiData.list.first().components

            val aqiCategory = when (aqiIndex) {
                1 -> aqiQualityCategories[0] // Good
                2 -> aqiQualityCategories[1] // Fair
                3 -> aqiQualityCategories[2] // Moderate
                4 -> aqiQualityCategories[3] // Poor
                5 -> aqiQualityCategories[4] // Very Poor
                else -> "Unknown"
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    contentColor = Color.Black
                ),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush = verticalGradient(
                            listOf(CardColor, Sunrise_color),
                        )),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        text = "Air Quality Index (AQI): \t$aqiIndex",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 40.dp),
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.Black)) {
                                    append("Category-\t")
                                }
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier.padding(end = 70.dp),
                            text = aqiCategory,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif,
                            color = when (aqiIndex) {
                                1 -> Color.Green
                                2 -> Color.Yellow
                                3 -> Color(color = 0xFFFF6F00)
                                4 -> Color(color = 0xFF4A148C)
                                5 -> Color.Red
                                else -> Color.Gray
                            }
                        )
                    }
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.DarkGray)
                    Text(modifier = Modifier.fillMaxWidth(),
                        text = "Pollutant Levels:",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "CO: ${components.co} µg/m³",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                "NO: ${components.no} µg/m³", fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                "NO₂: ${components.no2} µg/m³", fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Column {
                            Text(
                                "O₃: ${components.o3} µg/m³", fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                "PM₂.₅: ${components.pm2_5} µg/m³", fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                "PM₁₀: ${components.pm10} µg/m³", fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }
            }
        }


        @Composable
        fun SunriseSunSetDisplayCard(weatherData: WeatherResponse) {
            val colorGradient = linearGradient(
                listOf(Sunrise_color, Sunset_Color),
                start = Offset(0f, 1f), end = Offset(1f, 2f),
                tileMode = TileMode.Repeated
            )
            val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
            val sunriseTime = weatherData.sys.sunrise.let {
                Instant.ofEpochSecond(it.toLong())
                    .atZone(ZoneId.systemDefault())
                    .format(timeFormatter)
            } ?: "N/A"
            val sunsetTime = weatherData.sys.sunset.let {
                Instant.ofEpochSecond(it.toLong())
                    .atZone(ZoneId.systemDefault())
                    .format(timeFormatter)
            } ?: "N/A"
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp, horizontal = 25.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorGradient)
                        .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Image(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            painter = painterResource(R.drawable.app_feature_iccon),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            text = sunriseTime,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Image(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            painter = painterResource(R.drawable.sunset_image),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            text = sunsetTime,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }

        @Composable
        fun OtherWeatherData(weatherData: WeatherResponse) {
            val iconList = listOf(
                Icons.Rounded.Thermostat, Icons.Rounded.WaterDrop,
                Icons.Rounded.Air, Icons.Rounded.Visibility,
            )
            val labels = listOf("Feels like", "Humidity", "Air pressure", "Visibility", " Wind")
            val data = weatherData.main.let {
                listOf(
                    it.feels_like.toString(), it.humidity.toString(), it.pressure.toString(),
                    it.grnd_level, it.sea_level
                )
            }
            val latitude = weatherData.coord.lat
            val longitude = weatherData.coord.lon
            val countryName = weatherData.sys.country
            val timeZone = weatherData.timezone
            val wind = weatherData.wind
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = CardColor,
                            contentColor = Color.Black
                        ), elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .size(30.dp),
                            imageVector = iconList[0],
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .width(120.dp),
                            text = labels[0],
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .width(120.dp),
                            text = "${data[0]} °c",
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedCard(
                        modifier = Modifier.padding(10.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = CardColor,
                            contentColor = Color.Black
                        ), elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .size(30.dp),
                            imageVector = iconList[1],
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .width(120.dp),
                            text = labels[1],
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .width(120.dp),
                            text = "${data[1]} %",
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = CardColor,
                            contentColor = Color.Black
                        ), elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .padding(top = 1.dp),
                            alignment = Alignment.Center,
                            imageVector = iconList[2],
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            text = labels[4],
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                        HorizontalDivider(modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp),
                            thickness = 1.dp,
                            color = Color.DarkGray)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 5.dp),
                                text = "Speed: ${wind.speed} mph",
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 5.dp),
                                text = "Degree: ${wind.deg}°",
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.SansSerif
                            )
                        }

                    }
                }
            }
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 10.dp, horizontal = 25.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 10.dp
                ), colors = CardDefaults.elevatedCardColors(
                    containerColor = Sunrise_color,
                    contentColor = Color.Black
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ground level:",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = "${data[3]} hPa",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp,
                    horizontal = 10.dp),
                    thickness = 1.dp,
                    color = Color.DarkGray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 1.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Sea level:",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = "${data[4]} hPa",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = CardColor,
                            contentColor = Color.Black
                        ), elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .size(30.dp),
                            imageVector = iconList[2],
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .width(120.dp),
                            text = labels[2],
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .width(120.dp),
                            text = "${data[2]} hPa",
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    ElevatedCard(
                        modifier = Modifier.padding(10.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = CardColor,
                            contentColor = Color.Black
                        ), elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .size(30.dp),
                            imageVector = iconList[3],
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .width(120.dp),
                            text = labels[3],
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .width(120.dp),
                            text = "${data[3]} km",
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp, start = 15.dp, end = 15.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 10.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    contentColor = Color.Black
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Sunrise_color),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            text = "Latitude",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            text = "${latitude}°",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(45.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        androidx.compose.foundation.Canvas(
                            modifier = Modifier.height(40.dp)
                        ) {
                            drawLine(
                                color = Color.Black,
                                Offset(x = 0f, y = size.height),
                                Offset(x = 0f, y = 0f),
                                strokeWidth = 10.dp.value,
                                cap = StrokeCap.Round,
                                pathEffect = PathEffect.cornerPathEffect(10f),
                                blendMode = BlendMode.Darken
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            text = "Longitude",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            text = "${longitude}°",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 15.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 10.dp,
                ), colors = CardDefaults.elevatedCardColors(
                    contentColor = Color.Black
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Sunset_Color),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Image(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            painter = painterResource(R.drawable.location_icon),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            text = countryName,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Spacer(modifier = Modifier.width(100.dp))
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Image(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            painter = painterResource(R.drawable.clock),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .height(45.dp)
                                .padding(top = 10.dp),
                            text = formatTimeZone(timeZone = timeZone),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
    }
}

fun formatTimeZone(timeZone: Int): String {
    val hours = timeZone / 3600
    val minutes = (timeZone % 3600) / 60
    return "UTC%+03d:%02d".format(hours, minutes)
}