package ru.pomidorka.weatherapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityData(
    @PrimaryKey val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    val url: String,
)
