package ru.pomidorka.weatherapp.core.weatherapi

import ru.pomidorka.weatherapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiController {
    const val TOKEN = BuildConfig.WEATHER_API_TOKEN
    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    val weatherApi: WeatherApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)
    }
}