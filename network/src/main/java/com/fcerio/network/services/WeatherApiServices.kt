package com.fcerio.network.services

import com.fcerio.network.models.weather.WeatherDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiServices {
    @GET("v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherDTO

}