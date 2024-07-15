package com.fcerio.network.models.weather

import com.fcerio.domain.weather.WeatherInfo
import com.squareup.moshi.Json
import java.time.LocalDateTime

data class WeatherDTO(
    @field:Json(name = "hourly")
    val weatherData: WeatherDataDTO
)


fun WeatherDTO.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if(now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}