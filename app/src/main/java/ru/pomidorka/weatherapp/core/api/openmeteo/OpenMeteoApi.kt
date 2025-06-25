package ru.pomidorka.weatherapp.core.api.openmeteo

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.OpenMeteoData

interface OpenMeteoApi {
    @GET("forecast")
    suspend fun getForecastOfDays(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("minutely_15") minutely15: String = "temperature_2m,apparent_temperature",
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("timezone") timeZone: String = "auto"
    ): Response<OpenMeteoData>

    @GET("forecast")
    suspend fun getForecastOfDates(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("minutely_15") minutely15: String = "temperature_2m,apparent_temperature",
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("timezone") timeZone: String = "auto"
    ): Response<OpenMeteoData>
}