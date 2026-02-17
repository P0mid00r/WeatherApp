package ru.pomidorka.weatherapp.core.api.openmeteo.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import ru.pomidorka.weatherapp.R

fun Int.codeToConditionName() = conditionRuMap[this] ?: throw IllegalArgumentException()

enum class TimeOfDay(val value: String) {
    Night("night"),
    Day("day");

    val isDay
        get() = this == Day

    companion object {
        fun Int.parseTimeOfDay() = when(this) {
            0 -> Night
            1 -> Day
            else -> throw IllegalArgumentException()
        }

        fun Boolean.parseTimeOfDay() = when(this) {
            false -> Night
            true -> Day
        }
    }
}

fun Int.codeToUrlIcon(timeOfDay: TimeOfDay): String {
    val iconId = weatherCodeToIconCodeMap[this] ?: 113
    val timeOfDayName = timeOfDay.value
    return "https://cdn.weatherapi.com/weather/64x64/$timeOfDayName/$iconId.png"
}

@Composable
fun Int.codeToIcon(timeOfDay: TimeOfDay): Painter {
    val iconId = when(timeOfDay) {
        TimeOfDay.Day -> weatherCodeToDayIconDrawableId[this]
        TimeOfDay.Night -> weatherCodeToNightIconDrawableId[this]
    }

    return painterResource(iconId ?: throw IllegalArgumentException())
}

private val weatherCodeToDayIconDrawableId = mapOf<Int, Int>(
    0 to R.drawable.weather_icon_day_113,
    1 to R.drawable.weather_icon_day_116,
    2 to R.drawable.weather_icon_day_116,
    3 to R.drawable.weather_icon_day_122,
    45 to R.drawable.weather_icon_day_248,
    48 to R.drawable.weather_icon_day_260,
    51 to R.drawable.weather_icon_day_263,
    53 to R.drawable.weather_icon_day_266,
    55 to R.drawable.weather_icon_day_266,
    61 to R.drawable.weather_icon_day_296,
    63 to R.drawable.weather_icon_day_302,
    65 to R.drawable.weather_icon_day_308,
    66 to R.drawable.weather_icon_day_311,
    67 to R.drawable.weather_icon_day_314,
    71 to R.drawable.weather_icon_day_326,
    73 to R.drawable.weather_icon_day_332,
    75 to R.drawable.weather_icon_day_338,
    77 to R.drawable.weather_icon_day_350,
    80 to R.drawable.weather_icon_day_353,
    81 to R.drawable.weather_icon_day_356,
    82 to R.drawable.weather_icon_day_359,
    85 to R.drawable.weather_icon_day_368,
    86 to R.drawable.weather_icon_day_371,
    95 to R.drawable.weather_icon_day_200,
    96 to R.drawable.weather_icon_day_386,
    99 to R.drawable.weather_icon_day_389,
)

private val weatherCodeToNightIconDrawableId = mapOf<Int, Int>(
    0 to R.drawable.weather_icon_night_113,
    1 to R.drawable.weather_icon_night_116,
    2 to R.drawable.weather_icon_night_116,
    3 to R.drawable.weather_icon_night_122,
    45 to R.drawable.weather_icon_night_248,
    48 to R.drawable.weather_icon_night_260,
    51 to R.drawable.weather_icon_night_263,
    53 to R.drawable.weather_icon_night_266,
    55 to R.drawable.weather_icon_night_266,
    61 to R.drawable.weather_icon_night_296,
    63 to R.drawable.weather_icon_night_302,
    65 to R.drawable.weather_icon_night_308,
    66 to R.drawable.weather_icon_night_311,
    67 to R.drawable.weather_icon_night_314,
    71 to R.drawable.weather_icon_night_326,
    73 to R.drawable.weather_icon_night_332,
    75 to R.drawable.weather_icon_night_338,
    77 to R.drawable.weather_icon_night_350,
    80 to R.drawable.weather_icon_night_353,
    81 to R.drawable.weather_icon_night_356,
    82 to R.drawable.weather_icon_night_359,
    85 to R.drawable.weather_icon_night_368,
    86 to R.drawable.weather_icon_night_371,
    95 to R.drawable.weather_icon_night_200,
    96 to R.drawable.weather_icon_night_386,
    99 to R.drawable.weather_icon_night_389,
)

private val weatherCodeToIconCodeMap = mapOf<Int, Int>(
    // Clear sky
    0 to 113,  // 1000: Sunny/Clear

    // Mainly clear / Partly cloudy
    1 to 116,  // 1003: Partly cloudy
    2 to 116,  // 1003: Partly cloudy

    // Overcast
    3 to 122,  // 1009: Overcast

    // Fog
    45 to 248, // 1135: Fog
    48 to 260, // 1147: Freezing fog

    // Drizzle
    51 to 263, // 1150: Patchy light drizzle
    53 to 266, // 1153: Light drizzle
    55 to 266, // 1153: Light drizzle (ближайшее)

    // Rain
    61 to 296, // 1183: Light rain
    63 to 302, // 1189: Moderate rain
    65 to 308, // 1195: Heavy rain

    // Freezing rain
    66 to 311, // 1198: Light freezing rain
    67 to 314, // 1201: Moderate/heavy freezing rain

    // Snow fall
    71 to 326, // 1213: Light snow
    73 to 332, // 1219: Moderate snow
    75 to 338, // 1225: Heavy snow

    // Snow grains
    77 to 350, // 1237: Ice pellets

    // Rain showers
    80 to 353, // 1240: Light rain shower
    81 to 356, // 1243: Moderate/heavy rain shower
    82 to 359, // 1246: Torrential rain shower

    // Snow showers
    85 to 368, // 1255: Light snow showers
    86 to 371, // 1258: Moderate/heavy snow showers

    // Thunderstorm
    95 to 200, // 1087: Thundery outbreaks possible

    // Thunderstorm with hail
    96 to 386, // 1273: Patchy light rain with thunder
    99 to 389  // 1276: Moderate/heavy rain with thunder
)

private val conditionRuMap = mapOf<Int, String>(
    0 to "Ясно",

    1 to "В основном ясно",
    2 to "Переменная облачность",
    3 to "Пасмурно",

    45 to "Туман",
    48 to "Густой туман с изморозью",

    51 to "Легкая морось",
    53 to "Морось",
    55 to "Сильная морось",

    61 to "Небольшой дождь",
    63 to "Дождь",
    65 to "Сильный дождь",

    66 to "Легкий ледяной дождь",
    67 to "Ледяной дождь",

    71 to "Небольшой снег",
    73 to "Снег",
    75 to "Сильный снег",

    77 to "Снежная крупа",

    80 to "Небольшой ливень",
    81 to "Ливень",
    82 to "Сильный ливень",

    85 to "Небольшой снегопад",
    86 to "Снегопад",

    95 to "Гроза",

    96 to "Гроза с градом",
    99 to "Сильная гроза с градом",
)

private val conditionEnMap = mapOf<Int, String>(
    0 to "Clear sky",

    1 to "Mainly clear",
    2 to "Partly cloudy",
    3 to "Overcast",

    45 to "Fog",
    48 to "Depositing rime fog",

    51 to "Drizzle light",
    53 to "Drizzle moderate",
    55 to "Drizzle dense intensity",

    61 to "Rain slight",
    63 to "Rain moderate",
    65 to "Rain heavy intensity",

    66 to "Freezing rain light",
    67 to "Freezing rain heavy intensity",

    71 to "Snow fall slight",
    73 to "Snow fall moderate",
    75 to "Snow fall heavy intensity",

    77 to "Show grains",

    80 to "Rain showers slight",
    81 to "Rain showers moderate",
    82 to "Rain showers violent",

    85 to "Snow showers slight",
    86 to "Snow showers heavy",

    95 to "Thunderstorm slight",

    96 to "Thunderstorm with slight hail",
    99 to "Thunderstorm with heavy hail",
)