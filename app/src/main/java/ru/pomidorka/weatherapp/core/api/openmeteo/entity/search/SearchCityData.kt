package ru.pomidorka.weatherapp.core.api.openmeteo.entity.search

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName

@Stable
data class SearchCityData(
    @SerializedName("generationtime_ms") val generationTimeMs: Double,
    @SerializedName("results") val cities: List<City>?
)