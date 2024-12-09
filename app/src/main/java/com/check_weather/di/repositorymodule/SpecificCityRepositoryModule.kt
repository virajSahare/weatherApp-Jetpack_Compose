package com.check_weather.di.repositorymodule

import com.check_weather.domain.repository.SpecificCityRepository
import com.check_weather.domain.repositoryImpl.SpecificCityRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpecificCityRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSpecificCityRepository(repositoryImpl: SpecificCityRepositoryImpl): SpecificCityRepository
}