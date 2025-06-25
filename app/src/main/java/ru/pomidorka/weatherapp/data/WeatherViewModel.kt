package ru.pomidorka.weatherapp.data

import android.content.Context
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import okio.IOException
import ru.pomidorka.weatherapp.core.api.openmeteo.OpenMeteoApiController
import ru.pomidorka.weatherapp.core.api.weatherapi.WeatherApiController
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.Minutely15
import ru.pomidorka.weatherapp.data.database.AppDatabase
import ru.pomidorka.weatherapp.data.database.entity.CityData
import ru.pomidorka.weatherapp.core.api.weatherapi.entity.current.SearchData
import ru.pomidorka.weatherapp.core.api.weatherapi.entity.current.WeatherApiData
import ru.pomidorka.weatherapp.core.api.weatherapi.entity.current.convertToCityData
import ru.pomidorka.weatherapp.core.api.weatherapi.entity.forecast.ForecastData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherViewModel(val applicationContext: Context) : ViewModel() {
    private val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "weatherAppDb"
    ).build()

    var location by mutableStateOf<Location?>(null)

    var isLoadingData by mutableStateOf(true)
        private set

    var currentWeather: WeatherApiData? by mutableStateOf(null)
        private set
    var forecast: ForecastData? by mutableStateOf(null)
        private set

    var selectedCity: SearchData? by mutableStateOf(null)
        private set
    var favoritesCity = mutableStateListOf<SearchData>()
        private set

    var temperatureValues = mutableStateListOf<Double>()
        private set
    var timeValues = mutableStateListOf<String>()
        private set

    private var jobUpdaterWeatherInfo: Job? = null

    fun loadWeatherInfo() {
        isLoadingData = true
        viewModelScope.launch {
            async(Dispatchers.IO) {
                if (selectedCity == null) {
                    if (db.cityDataDao().count() == 0) {
                        db.cityDataDao().addCity(
                            CityData(1, "Барнаул", "Алтайский край", "Россия", 53.36f, 83.75f,"")
                        )
                    }

                    val cities = db.cityDataDao().getCities()
                    val searchDataes = mutableListOf<SearchData>()

                    cities.forEach {
                        searchDataes.add(SearchData(it.id, it.name, it.region, it.country, it.lat, it.lon, it.url))
                    }

                    favoritesCity.addAll(searchDataes)

                    selectedCity = favoritesCity[0]
                }
            }.await()

            val job1 = launch(Dispatchers.IO) {
                temperatureValues.clear()
                timeValues.clear()

                try {
                    val response = OpenMeteoApiController.openMeteoApi.getForecastOfDays(
                        latitude = selectedCity?.lat.toString(),
                        longitude = selectedCity?.lon.toString(),
                        forecastDays = 2
                    )

                    when(response.isSuccessful) {
                        true -> {
                            response.body()?.minutely_15?.let {
                                val currentDateTime = LocalDateTime.now()
                                val timeFirstIndex = it.time.indexOfFirst {
                                    it.contains("${"%02d".format(currentDateTime.hour)}:00")
                                }
                                val timeLastIndex = it.time.indexOfLast {
                                    it.contains("${"%02d".format(currentDateTime.hour)}:00")
                                }

                                for (i in 0..it.time.count() - 1) {
                                    if (i >= timeFirstIndex && i <= timeLastIndex) {
                                        val dateTime = LocalDateTime.parse(it.time[i])

                                        temperatureValues.add(listOf(it.temperature_2m[i], it.apparent_temperature[i]).average())
                                        val dateTimeString = dateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
                                        timeValues.add("$dateTimeString ${"%02d".format(dateTime.hour)}:${"%02d".format(dateTime.minute)}")
                                    }
                                }
                            }
                        }
                        false -> {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(applicationContext, response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } catch (ex: IOException) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(applicationContext, "Ошибка при получении данных с open-meteo", Toast.LENGTH_LONG).show()
                    }
                }
            }
            val job2 = launch(Dispatchers.IO) {
                try {
                    val response = WeatherApiController.weatherApi.getCurrentWeather(
                        WeatherApiController.TOKEN,
                        selectedCity?.name ?: "Барнаул",
                        "yes"
                    )

                    when(response.isSuccessful) {
                        true -> currentWeather = response.body()
                        false -> {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(applicationContext, response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } catch (ex: IOException) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(applicationContext, "Ошибка при получении данных с weatherapi", Toast.LENGTH_LONG).show()
                    }
                }
            }
            val job3 = launch(Dispatchers.IO) {
                try {
                    val response = WeatherApiController.weatherApi.getForecastWeather(
                        WeatherApiController.TOKEN,
                        selectedCity?.name ?: "Барнаул",
                        10,
                        "yes",
                    )

                    when(response.isSuccessful) {
                        true -> forecast = response.body()
                        false -> {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(applicationContext, response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } catch (ex: IOException) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(applicationContext, "Ошибка при получении данных с weatherapi", Toast.LENGTH_LONG).show()
                    }
                }
            }

            joinAll(job1, job2, job3)
            isLoadingData = false
        }
    }

    fun addCityToFavorites(searchData: SearchData) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.let {
                favoritesCity.add(it)

                db.cityDataDao().addCity(
                    it.convertToCityData()
                )
            }
        }
    }

    fun removeFavoritesCity(searchData: SearchData) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.let {
                favoritesCity.remove(it)

                db.cityDataDao().deleteCity(
                    it.convertToCityData()
                )
            }
        }
    }

    fun setCity(searchData: SearchData) {
        selectedCity = searchData
        loadWeatherInfo()
    }

    fun setEnabledUpdaterWeatherInfo(enabled: Boolean) {
        if (enabled) {
            jobUpdaterWeatherInfo ?: viewModelScope.launch {
                while (isActive) {
                    loadWeatherInfo()
                    delay(100000)
                }
            }
        } else {
            jobUpdaterWeatherInfo?.cancel()
            jobUpdaterWeatherInfo = null
        }
    }

    suspend fun searchCities(query: String): List<SearchData>? {
        return viewModelScope.async(Dispatchers.IO) {
            val response = WeatherApiController.weatherApi.search(
                WeatherApiController.TOKEN,
                query
            )

            return@async response.body()
        }.await()
    }

    suspend fun getWeathersOfDates(
        startDate: String,
        endDate: String
    ): Minutely15? {
       return viewModelScope.async {
            return@async try {
                val response = OpenMeteoApiController.openMeteoApi.getForecastOfDates(
                    latitude = selectedCity?.lat.toString(),
                    longitude = selectedCity?.lon.toString(),
                    startDate = startDate,
                    endDate = endDate
                )

                return@async when(response.isSuccessful) {
                    true -> {
                        response.body()?.minutely_15
                    }
                    false -> {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(applicationContext, "Ошибка при получении данных с open-meteo", Toast.LENGTH_LONG).show()
                        }

                        null
                    }
                }
            } catch (ex: IOException) {
                null
            }
        }.await()
    }
}