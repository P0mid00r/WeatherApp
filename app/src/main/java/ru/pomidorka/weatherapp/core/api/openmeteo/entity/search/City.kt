package ru.pomidorka.weatherapp.core.api.openmeteo.entity.search

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class City(
    @SerializedName("id") val id: Int,
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String,
    @SerializedName("admin1") val admin1: String?,
)