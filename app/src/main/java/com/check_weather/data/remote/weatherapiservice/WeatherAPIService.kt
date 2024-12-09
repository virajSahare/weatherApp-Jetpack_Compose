package com.check_weather.data.remote.weatherapiservice

import com.check_weather.data.model.WeatherAQIResponse
import com.check_weather.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("weather") //api end point
    //creating the function to get the weather from api
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String="b56d77168515b5f598f58295aac71027",
        @Query("units") units: String="metric"
    ):WeatherResponse
    @GET("air_pollution")
    suspend fun getAQI(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey:String="b56d77168515b5f598f58295aac71027"
    ):WeatherAQIResponse
}