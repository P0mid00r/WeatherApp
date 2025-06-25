package ru.pomidorka.weatherapp.core.api.weatherapi.entity.current

data class AirQuality(
    val co: Double = 0.0,
    val gb_defra_index: Int = 0,
    val no2: Double = 0.0,
    val o3: Double = 0.0,
    val pm10: Double = 0.0,
    val pm2_5: Double = 0.0,
    val so2: Double = 0.0,
    val us_epa_index: Int = 0
)