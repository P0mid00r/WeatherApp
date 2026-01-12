package ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely

import androidx.annotation.IntRange
import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName

@Stable
data class Minutely15(
    @SerializedName("apparent_temperature") val apparentTemperature: List<Double>,
    @SerializedName("temperature_2m") val temperature2m: List<Double>,
    @SerializedName("time") val time: List<String>,
    @SerializedName("is_day") val isDay: List<Int>,
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