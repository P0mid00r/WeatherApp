package ru.pomidorka.weatherapp.core.api.weatherapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.pomidorka.weatherapp.util.Constants

object WeatherApiController {
    const val TOKEN = Constants.WEATHER_API_TOKEN
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