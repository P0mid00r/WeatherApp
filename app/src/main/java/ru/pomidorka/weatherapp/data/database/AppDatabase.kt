package ru.pomidorka.weatherapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.pomidorka.weatherapp.data.database.dao.CityDataDao
import ru.pomidorka.weatherapp.data.database.entity.CityData

@Database(entities = [CityData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDataDao(): CityDataDao
}
