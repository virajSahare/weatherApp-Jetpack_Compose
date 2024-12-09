package com.check_weather.domain.repositoryImpl

import com.check_weather.data.local.CityNameSaverPref
import com.check_weather.domain.repository.SpecificCityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpecificCityRepositoryImpl @Inject constructor(
private val cityNameSaverPref:CityNameSaverPref
):SpecificCityRepository{
    override suspend fun saveSpecificCity(city: String) {
        cityNameSaverPref.saveCityName(key = "CityName", value = city)
    }

    override suspend fun getSpecificCity(key: String):String? {
      return cityNameSaverPref.getSpecificCityName(key = key)
    }

}