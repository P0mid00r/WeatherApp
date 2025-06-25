package ru.pomidorka.weatherapp.core.api.openmeteo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenMeteoApiController {
    private const val BASE_URL = "https://api.open-meteo.com/v1/"

    val openMeteoApi: OpenMeteoApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        openMeteoApi = retrofit.create(OpenMeteoApi::class.java)
    }
}