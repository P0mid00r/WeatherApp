package ru.pomidorka.weatherapp.core.api.weatherapi.entity

data class WeatherToday(
    val date: String,
    val temperatureDay: Double,
    val temperatureNight: Double,
    val condition: String,
    val icon: String
)