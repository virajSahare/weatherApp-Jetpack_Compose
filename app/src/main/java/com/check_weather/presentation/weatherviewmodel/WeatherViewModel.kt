package com.check_weather.presentation.weatherviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.check_weather.data.model.WeatherAQIResponse
import com.check_weather.data.model.WeatherResponse
import com.check_weather.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val
    repository: WeatherRepository
): ViewModel() {
    //creating immutable stable state flow which containing the WeatherResponse
    private val _weatherData= MutableStateFlow<Result<WeatherResponse>?>(null)
    /* creating stateFlow which hold the data from the MutableStateFlow
    observe the data to ui and the ui got automatically got updated
     */
    val weatherData:StateFlow<Result<WeatherResponse>?> = _weatherData
    /*
    creating following fun which accepting the city name as parameter
    in viewModelScope we calling getWeatherData fun from repo. by passing
    the city name as parameter and simultaneously collect the data from the
    repo. and assign the data to the _weatherData MutableStateFlow with latest data collection
     */
    private val _aqiData= MutableStateFlow<Result<WeatherAQIResponse>?>(null)
    val aqiData:StateFlow<Result<WeatherAQIResponse>?> = _aqiData

    fun fetchWeather(city:String){
        viewModelScope.launch {
            repository.getWeatherData(city=city).collect{
                _weatherData.value=it
            }
        }
    }
    fun fetchAQI(latitude:Double,longitude:Double){
        viewModelScope.launch {
            repository.getAQIData(latitude=latitude,longitude=longitude).collect{
                _aqiData.value=it
            }
        }
    }
}