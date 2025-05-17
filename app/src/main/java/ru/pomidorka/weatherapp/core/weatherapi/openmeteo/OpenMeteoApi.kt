package ru.pomidorka.weatherapp.core.weatherapi.openmeteo

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.pomidorka.weatherapp.core.weatherapi.openmeteo.entity.OpenMeteoData

interface OpenMeteoApi {
    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("forecast_days") forecastDays: Int = 7,
    ): Response<OpenMeteoData>
}