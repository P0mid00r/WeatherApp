package ru.pomidorka.weatherapp.data.entity.gismetio

data class Response(
    val city: Int,
    val cloudiness: Cloudiness,
    val date: Date,
    val description: Description,
    val gm: Int,
    val humidity: Humidity,
    val icon: String,
    val kind: String,
    val phenomenon: Int,
    val precipitation: Precipitation,
    val pressure: Pressure,
    val radiation: Radiation,
    val storm: Boolean,
    val temperature: Temperature,
    val wind: Wind
)