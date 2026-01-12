package ru.pomidorka.weatherapp.data

import ru.pomidorka.weatherapp.core.api.openmeteo.entity.CurrentWeather
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.OpenMeteoData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.City

data class MainScreenState(
    val isLoadingData: Boolean = true,
    val currentWeather: CurrentWeather? = null,
    val weather: OpenMeteoData? = null,
    val selectedCity: City? = null,
    val temperatureValues: List<Double> = emptyList(),
    val timeValues: List<String> = emptyList()
)
