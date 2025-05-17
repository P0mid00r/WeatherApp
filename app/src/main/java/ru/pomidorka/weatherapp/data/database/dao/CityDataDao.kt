package ru.pomidorka.weatherapp.data.database.dao

import androidx.room.Delete
import androidx.room.Query
import ru.pomidorka.weatherapp.data.database.entity.CityData

interface CityDataDao {
    @Query("SELECT COUNT(1) FROM citydata")
    fun count(): Int

    @Query("SELECT * FROM citydata")
    fun getCities(): List<CityData>

    @Delete
    fun delete()
}