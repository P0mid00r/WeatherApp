package ru.pomidorka.weatherapp.core.weatherapi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.pomidorka.weatherapp.BuildConfig


object GismetioController {
    private val TAG = javaClass.name
    private const val gismetioToken: String = BuildConfig.GISMETIO_API_TOKEN
    private const val gismetioBaseUrl: String = "https://api.gismeteo.net/v2/weather/"

    val gismetioApi: GismetioApi?

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(gismetioBaseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        gismetioApi = retrofit.create(GismetioApi::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .header("X-Gismeteo-Token", gismetioToken)
                .build()
            chain.proceed(newRequest)
        }.build()
    }
}