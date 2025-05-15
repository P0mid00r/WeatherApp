package ru.pomidorka.weatherapp.core.weatherapi

import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object GismetioControllerNew {
    private const val gismetioToken: String = "6414437a017a35.91123853"
    private const val gismetioBaseUrl: String = "https://api.gismeteo.net/v2/weather/"

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                // Отключаем SSL-проверку
                sslSocketFactory(createTrustAllSslSocketFactory(), createTrustAllManager())
                hostnameVerifier { _, _ -> true }
            }
            .build()
    }

    private fun createTrustAllSslSocketFactory(): SSLSocketFactory {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(createTrustAllManager()), java.security.SecureRandom())
        return sslContext.socketFactory
    }

    private fun createTrustAllManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        }
    }


    fun getCurrentWeather(id: Int): String {
        val request = Request.Builder()
            .url(gismetioBaseUrl + "current/${id}/?lang=ru")
            .get()
            .header("X-Gismeteo-Token", gismetioToken)
            .header("Content-Type", "application/json")
//            .header("Accept-Encoding", "deflate")
            .build()
        val response = getClient().newCall(request).execute()

        if (response.body != null) {
            return response.body!!.string().toString()
        }

        return "Пусто просто"
    }
}