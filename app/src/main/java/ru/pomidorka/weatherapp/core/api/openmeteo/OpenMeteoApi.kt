package ru.pomidorka.weatherapp.core.api.openmeteo

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.CurrentWeatherData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.OpenMeteoData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.SearchCityData

interface OpenMeteoApi {
    /**
     * url example: https://api.open-meteo.com/v1/forecast?latitude=53.3606&longitude=83.7636&daily=temperature_2m_max,temperature_2m_min,weather_code&timezone=auto&forecast_days=3&minutely_15=temperature_2m,apparent_temperature
     */
    @GET("forecast")
    @Cached(hours = 1)
    suspend fun getForecastOfDays(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("minutely_15") minutely15: String = "temperature_2m,apparent_temperature,is_day",
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("timezone") timeZone: String = "auto"
    ): Response<OpenMeteoData>

    @GET("forecast")
    @Cached(hours = 6)
    suspend fun getForecastOfDates(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("minutely_15") minutely15: String = "temperature_2m,apparent_temperature,is_day",
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("timezone") timeZone: String = "auto"
    ): Response<OpenMeteoData>

    /**
     * url example: https://api.open-meteo.com/v1/forecast?latitude=53.3606&longitude=83.7636&daily=temperature_2m_max,temperature_2m_min&current=temperature_2m,apparent_temperature,weather_code&timezone=auto&forecast_days=1
     */
    @GET("forecast")
    @Cached(minutes = 5)
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("current") current: String = "temperature_2m,apparent_temperature,weather_code",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 1
    ): Response<CurrentWeatherData>

    @GET("https://geocoding-api.open-meteo.com/v1/search")
    @Cached(days = 1)
    suspend fun search(
        @Query("name") query: String,
        @Query("language") language: String = "ru",
//        @Query("countryCode") countryCode: String = "RU",
    ): Response<SearchCityData>
}