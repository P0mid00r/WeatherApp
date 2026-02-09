package ru.pomidorka.weatherapp.data.repository

import kotlinx.serialization.Serializable
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.City

@Serializable
data class AppSettings(
    val versionData: Int = 1,
    val selectedCity: City = defaultCity,
    val favoritesCity: List<City> = listOf(defaultCity)
)

private val defaultCity = City(
    id = 524901,
    latitude = 55.75222f,
    longitude = 37.61556f,
    timezone = "Europe/Moscow",
    name = "Москва",
    country = "Россия",
    admin1 = "Москва"
)