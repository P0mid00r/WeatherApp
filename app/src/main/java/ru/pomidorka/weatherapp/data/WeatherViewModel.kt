package ru.pomidorka.weatherapp.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import kotlinx.coroutines.launch
import ru.pomidorka.weatherapp.core.weatherapi.openmeteo.OpenMeteoApiController
import ru.pomidorka.weatherapp.core.weatherapi.WeatherApiController
import ru.pomidorka.weatherapp.data.database.AppDatabase
import ru.pomidorka.weatherapp.data.entity.weatherapi.current.SearchData
import ru.pomidorka.weatherapp.data.entity.weatherapi.current.WeatherApiData
import ru.pomidorka.weatherapp.data.entity.weatherapi.forecast.ForecastData

class WeatherViewModel(val applicationContext: Context) : ViewModel() {
    private val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "weatherAppDb"
    ).build()

    var currentWeather: WeatherApiData? by mutableStateOf(null)
    var forecast: ForecastData? by mutableStateOf(null)

    var selectedCity: SearchData? by mutableStateOf(null)
    var favoritesCity = mutableStateListOf<SearchData>()

    var values = mutableStateListOf<Double>()

    private var jobUpdaterWeatherInfo: Job? = null

    init {
        favoritesCity.addAll(listOf(
            SearchData(1, "Барнаул", "Алтайский край", "Россия", 53.36f, 83.75f,""),
            SearchData(2, "Новоалтайск", "Алтайский край", "Россия", 123f,123f,""),
            SearchData(3, "Новосибирск", "123456789098765432101234567890123", "Россия", 123f,123f,""),
            SearchData(4, "Москва", "123", "Россия", 123f,123f,""),
            SearchData(5, "Санкт-Петербург", "123", "Россия", 123f,123f,""),
            SearchData(6, "Екатеринбург", "123", "Россия", 123f,123f,""),
        ))
    }

    fun loadWeatherInfo() {
        if (selectedCity == null) {
            selectedCity = favoritesCity[0]
        }

        viewModelScope.launch {
            launch(Dispatchers.IO) {
                values.clear()
                val response = OpenMeteoApiController.openMeteoApi.getForecast(
                    latitude = selectedCity?.lat.toString(),
                    longitude = selectedCity?.lon.toString(),
                    forecastDays = 1
                )

                when(response.isSuccessful) {
                    true -> {
                        response.body()?.hourly?.temperature_2m?.forEach { values.add(it) }
                    }
                    false -> {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(applicationContext, response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            launch(Dispatchers.IO) {
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
            }
            launch(Dispatchers.IO) {
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
                    delay(10000)
                }
            }
        } else {
            jobUpdaterWeatherInfo?.cancel()
            jobUpdaterWeatherInfo = null
        }
    }

    suspend fun searchCities(query: String): List<SearchData>? {
        return viewModelScope.async {
            val response = WeatherApiController.weatherApi.search(
                WeatherApiController.TOKEN,
                query
            )

            return@async response.body()
        }.await()
    }
}