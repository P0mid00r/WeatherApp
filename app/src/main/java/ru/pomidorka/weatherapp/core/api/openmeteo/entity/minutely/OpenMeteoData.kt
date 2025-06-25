package ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely

data class OpenMeteoData(
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val minutely_15: Minutely15,
    val minutely_15_units: Minutely15Units,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)