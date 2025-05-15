package ru.pomidorka.weatherapp.data.entity.gismetio

data class Date(
    val UTC: String,
    val hr_to_forecast: Any,
    val local: String,
    val time_zone_offset: Int,
    val unix: Int
)