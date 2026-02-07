package ru.pomidorka.weatherapp.core.api.openmeteo.entity.search

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Stable
@Serializable
data class SearchCityData(
    @SerialName("generationtime_ms") val generationTimeMs: Double,
    @SerialName("results") val cities: List<City>?
)