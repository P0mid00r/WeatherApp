package ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely

data class Minutely15Units(
    val apparent_temperature: String,
    val temperature_2m: String,
    val time: String
)