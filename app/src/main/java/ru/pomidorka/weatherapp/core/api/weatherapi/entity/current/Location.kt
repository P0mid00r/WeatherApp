package ru.pomidorka.weatherapp.core.api.weatherapi.entity.current

data class Location(
    val country: String = "",
    val lat: Double = 0.0,
    val localtime: String = "",
    val localtime_epoch: Int = 0,
    val lon: Double = 0.0,
    val name: String = "",
    val region: String = "",
    val tz_id: String = ""
)