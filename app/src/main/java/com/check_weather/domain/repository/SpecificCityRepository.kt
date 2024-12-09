package com.check_weather.domain.repository

import com.check_weather.data.local.CityNameSaverPref
import kotlinx.coroutines.flow.Flow

interface SpecificCityRepository {
    //creating suspend fun for saving and getting the specific city name
   suspend fun saveSpecificCity(city: String)
   suspend fun getSpecificCity(key:String):String?
}