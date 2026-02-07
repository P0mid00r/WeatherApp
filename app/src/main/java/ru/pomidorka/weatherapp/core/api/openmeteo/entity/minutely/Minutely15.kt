package ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely

import androidx.annotation.IntRange
import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Minutely15(
    @SerialName("apparent_temperature") val apparentTemperature: List<Double>,
    @SerialName("temperature_2m") val temperature2m: List<Double>,
    @SerialName("time") val time: List<String>,
    @SerialName("is_day") val isDay: List<Int>,
)

fun Minutely15.skipDays(
    @IntRange(from = 1, to = 9) days: Int
): Minutely15 {
    val startIndex = days * 24 * 4 - 1
    val lastIndex = this.apparentTemperature.lastIndex
    return Minutely15(
        apparentTemperature = this.apparentTemperature.subList(startIndex, lastIndex),
        temperature2m = this.temperature2m.subList(startIndex, lastIndex),
        time = this.time.subList(startIndex, lastIndex),
        isDay = this.isDay.subList(startIndex, lastIndex),
    )
}