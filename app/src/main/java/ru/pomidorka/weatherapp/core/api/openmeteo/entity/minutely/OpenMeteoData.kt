package ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely

import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.CurrentData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.DailyData

@Stable
data class OpenMeteoData(
    @SerializedName("generationtime_ms") val generationTimeMs: Double,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Int,
    @SerializedName("elevation") val elevation: Double,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("current") val current: CurrentData,
    @SerializedName("daily") val daily: DailyData,
    @SerializedName("minutely_15") val minutely15: Minutely15,
)