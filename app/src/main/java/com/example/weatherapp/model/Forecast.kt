package com.example.weatherapp.model

import java.io.Serializable

data class Forecast(
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
): Serializable