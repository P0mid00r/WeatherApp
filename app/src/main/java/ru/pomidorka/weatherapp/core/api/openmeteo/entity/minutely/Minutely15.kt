package ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely

data class Minutely15(
    val apparent_temperature: List<Double>,
    val temperature_2m: List<Double>,
    val time: List<String>
)