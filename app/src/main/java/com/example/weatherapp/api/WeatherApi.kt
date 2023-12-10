package com.example.weatherapp.api

import com.example.weatherapp.model.BaseWeather
import com.example.weatherapp.model.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.StringBuilder

// yocohe3315@topyte.com
interface WeatherApi {
    @GET("weather")
    suspend fun getBaseWeather(
        @Query("appid") appid: String, @Query("q") q: String, @Query("units") units: String
    ): Response<BaseWeather>

    @GET("onecall")
    suspend fun getForecast(
        @Query("appid") appid: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("exclude") exclude: String
    ): Response<Forecast>
}
