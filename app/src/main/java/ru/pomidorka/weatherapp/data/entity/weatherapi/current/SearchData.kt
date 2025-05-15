package ru.pomidorka.weatherapp.data.entity.weatherapi.current

data class SearchData(
    val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    val url: String,
)
