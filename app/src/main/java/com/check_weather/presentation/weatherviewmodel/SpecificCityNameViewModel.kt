package com.check_weather.presentation.weatherviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.check_weather.domain.repository.SpecificCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecificCityNameViewModel @Inject constructor(
 private val specificCityRepository: SpecificCityRepository
): ViewModel() {
    //creating immutable state flow to observe the city name
    private val _cityName= MutableStateFlow<String?>(null)
    /* creating the mutable state flow to expose current value
    and by this we can observe state changes in the UI, also we cannot
    modify it's value directly. also it is responsible to expose current state
    to UI.
     */
    val cityName: StateFlow<String?> =_cityName
    fun saveSpecificCityName(cityName:String){
        viewModelScope.launch {
            specificCityRepository.saveSpecificCity(city = cityName)
        }
    }
    fun getSpecificCityName(key:String){
        viewModelScope.launch {
            _cityName.value=specificCityRepository.getSpecificCity(key = key)
        }
    }
}