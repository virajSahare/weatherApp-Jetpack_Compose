package com.check_weather.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CityNameSaverPref(val context: Context) {
    //creating the extension function over Context class
    private val Context.dataStore by preferencesDataStore("CityName")

    suspend fun saveCityName(key:String,value:String){
    context.dataStore.edit {
        it[stringPreferencesKey(key)]=value
    }
    }
   suspend fun getSpecificCityName(key:String):String?{
       //mapping the save key to the value
       return context.dataStore.data.map {
           it[stringPreferencesKey(key)]
       }.first()
   }
}