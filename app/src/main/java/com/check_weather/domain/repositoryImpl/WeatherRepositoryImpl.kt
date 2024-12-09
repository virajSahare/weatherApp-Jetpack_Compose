package com.check_weather.domain.repositoryImpl

import com.check_weather.data.model.WeatherAQIResponse
import com.check_weather.data.model.WeatherResponse
import com.check_weather.data.remote.weatherapiservice.WeatherAPIService
import com.check_weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherAPIService
):WeatherRepository {
    override fun getWeatherData(city:String): Flow<Result<WeatherResponse>> = flow{
     try{
         val data=apiService.getWeather(city=city)
         emit(Result.success(data))
     }catch (e:Exception){
      emit(Result.failure(e))
     }
    }

    override fun getAQIData(latitude: Double, longitude: Double): Flow<Result<WeatherAQIResponse>> =
        flow {
      try{
          val data=apiService.getAQI(latitude=latitude, longitude = longitude)
          emit(Result.success(data))
      } catch (e:Exception){
          emit(Result.failure(e))
      }
    }
}