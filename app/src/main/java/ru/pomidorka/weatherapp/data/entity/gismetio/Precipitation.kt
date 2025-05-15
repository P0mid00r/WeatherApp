package ru.pomidorka.weatherapp.data.entity.gismetio

data class Precipitation(
    val amount: Double,
    val correction: Any,
    val duration: Int,
    val intensity: Int,
    val type: Int,
    val type_ext: Int
)