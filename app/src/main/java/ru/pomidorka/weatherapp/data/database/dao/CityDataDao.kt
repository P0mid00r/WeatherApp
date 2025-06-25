package ru.pomidorka.weatherapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.pomidorka.weatherapp.data.database.entity.CityData

@Dao
interface CityDataDao {
    @Query("SELECT COUNT(1) FROM citydata")
    fun count(): Int

    @Query("SELECT * FROM citydata")
    fun getCities(): List<CityData>

    @Insert
    fun addCity(cityData: CityData)

    @Insert
    fun addCities(vararg cityDataList: CityData)

    @Delete
    fun deleteCity(cityData: CityData)

    @Delete
    fun deleteCities(vararg cityDataDao: CityData)
}