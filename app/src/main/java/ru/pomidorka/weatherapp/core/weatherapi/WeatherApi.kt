package ru.pomidorka.weatherapp.core.weatherapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.pomidorka.weatherapp.data.entity.weatherapi.current.SearchData
import ru.pomidorka.weatherapp.data.entity.weatherapi.current.WeatherApiData
import ru.pomidorka.weatherapp.data.entity.weatherapi.forecast.ForecastData

interface WeatherApi {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") city: String,
        @Query("aqi") aqi: String,
        @Query("lang") lang: String = "ru"
    ): Response<WeatherApiData>

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("key") key: String,
        @Query("q") city: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String = "no",
        @Query("lang") lang: String = "ru"
    ): Response<ForecastData>

    @GET("search.json")
    suspend fun search(
        @Query("key") key: String,
        @Query("q") query: String
    ): Response<List<SearchData>>
}