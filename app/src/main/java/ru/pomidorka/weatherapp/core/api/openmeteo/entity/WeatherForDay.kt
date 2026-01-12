package ru.pomidorka.weatherapp.core.api.openmeteo.entity

data class WeatherForDay(
    val date: String,
    val temperatureMax: Float,
    val temperatureMin: Float,
    val condition: String,
    val icon: String,
)