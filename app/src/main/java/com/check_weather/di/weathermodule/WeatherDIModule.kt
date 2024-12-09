package com.check_weather.di.weathermodule

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.check_weather.data.baseurl.BASE_URL
import com.check_weather.data.local.CityNameSaverPref
import com.check_weather.data.remote.weatherapiservice.WeatherAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherDIModule {
    @Provides
    @Singleton
    //providing retrofit instance
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(
            BASE_URL
        ).client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //creating the function to provide the weather api service to retrofit
   @Provides
   @Singleton
   fun provideWeatherAPIService(retrofit: Retrofit): WeatherAPIService {
   return  retrofit.create(WeatherAPIService::class.java)
   }
    @Provides
    @Singleton
    fun provideCityNameSaverPref(@ApplicationContext context: Context):CityNameSaverPref{
    return CityNameSaverPref(context)
    }
}