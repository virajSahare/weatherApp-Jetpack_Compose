package com.check_weather.di.repositorymodule

import com.check_weather.domain.repository.WeatherRepository
import com.check_weather.domain.repositoryImpl.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//creating module abstract class for binding the repository and it's impl class
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
@Binds
@Singleton
/*
we don't need to call this function explicitly
because hilt will automatically call it at runtime
when instance of WeatherRepository is requested for injection.
 */
abstract fun bindWeatherRepository(
    weatherRepositoryImpl: WeatherRepositoryImpl
):WeatherRepository
}