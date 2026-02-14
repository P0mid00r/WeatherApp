package ru.pomidorka.weatherapp.core.api.openmeteo

import android.content.Context
import android.util.Log
import androidx.annotation.IntRange
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.pomidorka.weatherapp.core.Result
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.CurrentWeather
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.Minutely15
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.OpenMeteoData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.City
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.toCurrentWeather
import java.io.File

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Cached(
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0
)

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val cacheDuration = request.tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(Cached::class.java)
            ?.let {
                it.days.times(86400) + it.hours.times(3600) + it.minutes.times(60) + it.seconds
            }

        return if (cacheDuration != null) {
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$cacheDuration")
                .build()
        } else response
    }
}

class OpenMeteo(context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false

    }
    private val cacheSize = 20 * 1024 * 1024L // 10 MB

    private val openMeteoApi: OpenMeteoApi by lazy {
        val cacheDir = File(context.cacheDir, "http_cache")
        val cache = Cache(cacheDir, cacheSize)
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(CacheInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=utf-8".toMediaType())
            )
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
            response.log("getCurrentWeather")

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
            response.log("getForecastOfDays")

            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Failure(Throwable(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
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
            response.log("getForecastOfDates")

            if (response.isSuccessful) {
                Result.Success(response.body()!!.minutely15)
            } else {
                Result.Failure(Throwable(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    suspend fun search(query: String): Result<List<City>> {
        return try {
            val response = openMeteoApi.search(
                query = query,
            )
            response.log("search")

            if (response.isSuccessful) {
                Result.Success(response.body()?.cities ?: emptyList())
            } else {
                Result.Failure(Throwable(response.errorBody()?.string()))
            }
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    fun <T> retrofit2.Response<T>.log(name: String) {
        val raw = this.raw()
        if (raw.cacheResponse != null) {
            Log.d("Cache-$name", "📦 Данные из кэша")
        }
        if (raw.networkResponse != null) {
            Log.d("Cache-$name", "🌐 Данные из сети")
        }
    }

    companion object {
        private const val BASE_URL = "https://api.open-meteo.com/v1/"
    }
}