package ru.pomidorka.weatherapp.core.api.weatherapi.entity.forecast

data class ForecastData(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)