package ru.pomidorka.weatherapp.core.api.openmeteo

import androidx.annotation.IntRange
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.pomidorka.weatherapp.core.Result
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.CurrentWeather
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.Minutely15
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.OpenMeteoData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.City
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.SearchCityData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.toCurrentWeather

object OpenMeteo {
    private const val BASE_URL = "https://api.open-meteo.com/v1/"

    val openMeteoApi: OpenMeteoApi by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(OpenMeteoApi::class.java)
    }

    suspend fun getCurrentWeather(
        latitude: Float,
        longitude: Float,
    ): Result<CurrentWeather> {
        return try {
            val response = openMeteoApi.getCurrentWeather(
                latitude = latitude.toString(),
                longitude = longitude.toString()
            )

            if (response.isSuccessful) {
                val currentWeather = response.body()!!.toCurrentWeather()
                Result.Success(currentWeather)
            } else {
                Result.Failure(Throwable(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    suspend fun getForecastOfDays(
        latitude: Float,
        longitude: Float,
        @IntRange(from = 1, to = 10) forecastDays: Int = 2
    ): Result<OpenMeteoData> {
        return try {
            val response = openMeteoApi.getForecastOfDays(
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                forecastDays = forecastDays
            )

            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Failure(Throwable(response.errorBody()?.string()))
            }
        } catch (ex: IOException) {
            Result.Failure(ex)
        }
    }

    suspend fun getForecastOfDates(
        latitude: Float,
        longitude: Float,
        startDate: String,
        endDate: String,
    ): Result<Minutely15> {
        return try {
            val response = openMeteoApi.getForecastOfDates(
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                startDate = startDate,
                endDate = endDate
            )

            if (response.isSuccessful) {
                Result.Success(response.body()!!.minutely15)
            } else {
                Result.Failure(Throwable(response.errorBody()?.string()))
            }
        } catch (ex: IOException) {
            Result.Failure(ex)
        }
    }

    suspend fun search(query: String): Result<SearchCityData> {
        return try {
            val response = openMeteoApi.search(
                query = query,
            )

            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Failure(Throwable(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}