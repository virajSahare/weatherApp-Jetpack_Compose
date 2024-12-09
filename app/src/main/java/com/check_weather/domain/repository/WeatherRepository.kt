package com.check_weather.domain.repository

import com.check_weather.data.model.WeatherAQIResponse
import com.check_weather.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

/* creating the interface for the repository
by extending flow class by giving weather response model
 */
interface WeatherRepository
{
    fun getWeatherData(city:String): Flow<Result<WeatherResponse>>//weather model which the api will returns
    fun getAQIData(latitude:Double,longitude:Double):Flow<Result<WeatherAQIResponse>>
}