package ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.CurrentData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.DailyData

@Stable
@Serializable
data class OpenMeteoData(
    @SerialName("generationtime_ms") val generationTimeMs: Double,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    @SerialName("elevation") val elevation: Double,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    @SerialName("timezone") val timezone: String,
    @SerialName("latitude") val latitude: Float,
    @SerialName("longitude") val longitude: Float,
    @SerialName("current") val current: CurrentData?,
    @SerialName("daily") val daily: DailyData,
    @SerialName("minutely_15") val minutely15: Minutely15,
)