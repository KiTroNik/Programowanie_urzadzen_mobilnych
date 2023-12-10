package com.example.weatherapp.repository

import com.example.weatherapp.api.WeatherApi
import java.lang.StringBuilder
import javax.inject.Inject

class WeatherRepository
@Inject
constructor(private val weatherApi: WeatherApi) {
    suspend fun getBaseWeather(appid: String, q: String, units: String) = weatherApi.getBaseWeather(appid, q, units)
    suspend fun getForecast(appid: String, lat: Double, lon: Double, exclude: String, units: String) = weatherApi.getForecast(appid, lat, lon, units, exclude)
}
