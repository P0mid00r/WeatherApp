package ru.pomidorka.weatherapp.core.api.openmeteo.entity.search

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class City(
    @SerialName("id") val id: Int,
    @SerialName("latitude") val latitude: Float,
    @SerialName("longitude") val longitude: Float,
    @SerialName("timezone") val timezone: String,
    @SerialName("name") val name: String,
    @SerialName("country") val country: String,
    @SerialName("admin1") val admin1: String?,
)