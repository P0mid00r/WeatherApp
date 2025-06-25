package ru.pomidorka.weatherapp.core.api.weatherapi.entity.current

import ru.pomidorka.weatherapp.data.database.entity.CityData

data class SearchData(
    val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    val url: String,
)

fun SearchData.convertToCityData(): CityData {
    this.let {
        return CityData(
            it.id,
            it.name,
            it.region,
            it.country,
            it.lat,
            it.lon,
            it.url
        )
    }
}