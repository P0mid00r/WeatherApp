package ru.pomidorka.weatherapp.data

data class WeatherToday(
    val date: String,
    val temperatureDay: Double,
    val temperatureNight: Double,
    val condition: String,
    val icon: String
)
