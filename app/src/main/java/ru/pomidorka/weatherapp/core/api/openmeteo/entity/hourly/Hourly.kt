package ru.pomidorka.weatherapp.core.api.openmeteo.entity.hourly

data class Hourly(
    val temperature_2m: List<Double>,
    val time: List<String>
)