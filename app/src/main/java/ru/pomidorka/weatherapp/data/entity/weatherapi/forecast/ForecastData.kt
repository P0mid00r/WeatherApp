package ru.pomidorka.weatherapp.data.entity.weatherapi.forecast

data class ForecastData(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)