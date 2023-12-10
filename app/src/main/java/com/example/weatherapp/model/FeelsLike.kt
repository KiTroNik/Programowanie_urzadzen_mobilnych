package com.example.weatherapp.model

import java.io.Serializable

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
): Serializable