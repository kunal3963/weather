package com.practice.weather.di

import com.practice.weather.model.AppRepository
import com.practice.weather.model.AppService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): AppService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: AppService): AppRepository {
        return AppRepository(api)
    }
}