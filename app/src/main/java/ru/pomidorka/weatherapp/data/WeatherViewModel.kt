package ru.pomidorka.weatherapp.data

import android.content.Context
import android.location.Location
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.pomidorka.weatherapp.core.Result
import ru.pomidorka.weatherapp.core.api.openmeteo.OpenMeteo
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.Minutely15
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.City
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherViewModel(private val applicationContext: Context) : ViewModel() {
    private val repository = AppRepository(applicationContext)

    private var _mainScreenState = MutableStateFlow(MainScreenState())
    val mainScreenState = _mainScreenState.asStateFlow()

    var location by mutableStateOf<Location?>(null)

    var favoritesCity = mutableStateListOf<City>()
        private set

    private var jobUpdaterWeatherInfo: Job? = null

    init {
        loadSettings()
        loadWeatherInfo()
        setEnabledUpdaterWeatherInfo(false)
    }

    private fun loadSettings() {
        repository.apply {
            favoritesCity.addAll(appData.favoritesCity)
            _mainScreenState.update {
                MainScreenState(
                    selectedCity = appData.selectedCity,
                )
            }

            if (mainScreenState.value.selectedCity == null && appData.favoritesCity.isEmpty()) {
                val city = City(
                    id = 524901,
                    latitude = 55.75222f,
                    longitude = 37.61556f,
                    timezone = "Europe/Moscow",
                    name = "Москва",
                    country = "Россия",
                    admin1 = "Москва"
                )
                favoritesCity.add(city)
                _mainScreenState.update {
                    it.copy(selectedCity = favoritesCity[0])
                }
                appData = appData.copy(
                    selectedCity = city,
                    favoritesCity = favoritesCity
                )
                repository.save()
            }
        }
    }

    fun loadWeatherInfo() {
        _mainScreenState.update {
            MainScreenState(
                selectedCity = it.selectedCity
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val selectedCity = _mainScreenState.value.selectedCity!!

            val currentWeatherResponse = async { OpenMeteo.getCurrentWeather(selectedCity.latitude, selectedCity.longitude) }
            val forecastOfDaysResponse = async { OpenMeteo.getForecastOfDays(selectedCity.latitude, selectedCity.longitude) }
            val forecastFor7DaysResponse = async { OpenMeteo.getForecastOfDays(selectedCity.latitude, selectedCity.longitude, 7) }

            when(val currentWeather = currentWeatherResponse.await()) {
                is Result.Success -> {
                    _mainScreenState.update {
                        it.copy(currentWeather = currentWeather.data)
                    }
                }
                is Result.Failure -> {
                    showToast(currentWeather.throwable.toString())
                }
            }

            when(val forecastOfDays = forecastOfDaysResponse.await()) {
                is Result.Success -> {
                    forecastOfDays.data.minutely15.let {
                        val currentDateTime = LocalDateTime.now()
                        val timeFirstIndex = it.time.indexOfFirst {
                            it.contains("${"%02d".format(currentDateTime.hour)}:00")
                        }
                        val timeLastIndex = it.time.indexOfLast {
                            it.contains("${"%02d".format(currentDateTime.hour)}:00")
                        }

                        val temperatureValues = mutableListOf<Double>()
                        val timeValues = mutableListOf<String>()
                        for (i in 0..it.time.count() - 1) {
                            if (i >= timeFirstIndex && i <= timeLastIndex) {
                                val dateTime = LocalDateTime.parse(it.time[i])

                                temperatureValues.add(listOf(it.temperature2m[i], it.apparentTemperature[i]).average())
                                val dateTimeString = dateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
                                timeValues.add("$dateTimeString ${"%02d".format(dateTime.hour)}:${"%02d".format(dateTime.minute)}")
                            }
                        }

                        _mainScreenState.update { state ->
                            state.copy(
                                temperatureValues = temperatureValues,
                                timeValues = timeValues
                            )
                        }
                    }
                }
                is Result.Failure -> {
                    showToast(forecastOfDays.throwable.message.toString())
                }
            }

            when (val response = forecastFor7DaysResponse.await()) {
                is Result.Success -> {
                    _mainScreenState.update { state ->
                        state.copy(
                            weather = response.data,
                        )
                    }
                }
                is Result.Failure -> {
                    showToast(response.throwable.message.toString())
                }
            }

            _mainScreenState.update {
                it.copy(isLoadingData = false)
            }
        }
    }

    fun showToast(message: String) {
        Log.d("TEST", message)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun addCityToFavorites(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            city.let {
                favoritesCity.add(it)

                repository.apply {
                    appData = appData.copy(
                        favoritesCity = favoritesCity
                    )
                }.save()
            }
        }
    }

    // TODO при удалении выделенный город остается
    fun removeFavoriteCity(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            city.let {
                favoritesCity.remove(it)

                repository.apply {
                    appData = appData.copy(
                        favoritesCity = favoritesCity
                    )
                }.save()
            }
        }
    }

    fun setFavoriteCity(city: City) {
        _mainScreenState.update { it.copy(selectedCity = city) }
        repository.apply {
            appData = appData.copy(
                selectedCity = city
            )
        }.save()
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

    suspend fun isFavoriteCityContains(city: City): Boolean {
        return repository.appData.favoritesCity.contains(city)
    }

    suspend fun searchCities(query: String): List<City> {
        return when (val response = OpenMeteo.search(query)) {
            is Result.Success -> response.data.cities ?: emptyList()
            is Result.Failure -> {
                showToast(response.throwable.message ?: "")

                emptyList()
            }
        }
    }

    suspend fun getWeathersOfDates(
        startDate: String,
        endDate: String
    ): Minutely15? {
        val city = mainScreenState.value.selectedCity ?: return null
        val response = OpenMeteo.getForecastOfDates(
            latitude = city.latitude,
            longitude = city.longitude,
            startDate = startDate,
            endDate = endDate
        )

        return when(response) {
            is Result.Success -> response.data
            is Result.Failure -> {
                showToast(response.throwable.message ?: "")

                null
            }
        }
    }
}