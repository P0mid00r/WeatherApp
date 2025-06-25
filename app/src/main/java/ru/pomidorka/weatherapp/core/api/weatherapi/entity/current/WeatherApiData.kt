package ru.pomidorka.weatherapp.core.api.weatherapi.entity.current

data class WeatherApiData(
    val current: Current,
    val location: Location
)