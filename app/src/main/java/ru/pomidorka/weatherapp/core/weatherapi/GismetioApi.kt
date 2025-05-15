package ru.pomidorka.weatherapp.core.weatherapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.pomidorka.weatherapp.data.entity.gismetio.WeatherDataGismetio

interface GismetioApi {

    @GET("current/{id}/?lang=ru")
    suspend fun getWeatherToday(@Path("id") idRegion: Int): Response<WeatherDataGismetio>
}
    //@GET("")
    //suspend fun getCurrentTemperature()
    //@GET("")
    //suspend fun getWeatherMonth()
