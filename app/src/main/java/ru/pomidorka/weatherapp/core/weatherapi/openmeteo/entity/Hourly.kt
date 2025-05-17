package ru.pomidorka.weatherapp.core.weatherapi.openmeteo.entity

data class Hourly(
    val temperature_2m: List<Double>,
    val time: List<String>
)