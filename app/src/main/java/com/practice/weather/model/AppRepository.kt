package com.practice.weather.model

import javax.inject.Inject

class AppRepository @Inject constructor(
    private val api: AppService
) {
    suspend fun getWeather(city: String, apiKey: String): Result<ApiResponse> {
        return try {
            val response = api.getWeather(city, apiKey)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No data"))
            } else {
                Result.failure(Exception("Error fetching data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}