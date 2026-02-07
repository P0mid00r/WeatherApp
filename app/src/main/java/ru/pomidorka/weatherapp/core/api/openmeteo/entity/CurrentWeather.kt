package ru.pomidorka.weatherapp.core.api.openmeteo.entity

import androidx.annotation.IntRange
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.OpenMeteoData

data class CurrentWeather(
    val condition: String,
    val temperature: Float,
    val apparentTemperature: Float,
    val temperatureMax: Float,
    val temperatureMin: Float,
)

fun OpenMeteoData.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        condition = current!!.weatherCode.codeToConditionName(),
        temperature = current.temperature,
        apparentTemperature = current.apparentTemperature,
        temperatureMax = daily.maxTemps.first(),
        temperatureMin = daily.minTemps.first()
    )
}

fun CurrentWeatherData.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        condition = current.weatherCode.codeToConditionName(),
        temperature = current.temperature,
        apparentTemperature = current.apparentTemperature,
        temperatureMax = daily.maxTemps.first(),
        temperatureMin = daily.minTemps.first()
    )
}

@Serializable
data class CurrentWeatherData(
    @SerialName("current") val current: CurrentData,
    @SerialName("daily") val daily: DailyData
)

@Serializable
data class CurrentData(
    @SerialName("weather_code") val weatherCode: Int,
    @SerialName("temperature_2m") val temperature: Float,
    @SerialName("apparent_temperature") val apparentTemperature: Float
)

@Serializable
data class DailyData(
    @SerialName("time") val time: List<String>,
    @SerialName("weather_code") val weatherCode: List<Int>,
    @SerialName("temperature_2m_max") val maxTemps: List<Float>,
    @SerialName("temperature_2m_min") val minTemps: List<Float>
)

fun DailyData.skipDays(
    @IntRange(from = 1, to = 9) days: Int
): DailyData {
    val startIndex = days
    val lastIndex = this.time.lastIndex + 1
    return DailyData(
        time = this.time.subList(startIndex, lastIndex),
        weatherCode = this.weatherCode.subList(startIndex, lastIndex),
        maxTemps = this.maxTemps.subList(startIndex, lastIndex),
        minTemps = this.minTemps.subList(startIndex, lastIndex),
    )
}

fun DailyData.toWeatherForDayList() = mutableListOf<WeatherForDay>().apply {
    val data = this@toWeatherForDayList
    for (i in 0..data.weatherCode.size - 1) {
        add(WeatherForDay(
            date = data.time[i],
            temperatureMax = data.maxTemps[i],
            temperatureMin = data.minTemps[i],
            condition = data.weatherCode[i].codeToConditionName(),
            icon = data.weatherCode[i].codeToUrlIcon(TimeOfDay.Day)
        ))
    }
}
