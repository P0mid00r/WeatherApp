package ru.pomidorka.weatherapp

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.imageLoader
import coil3.request.CachePolicy
import coil3.request.crossfade
import ru.ok.tracer.CoreTracerConfiguration
import ru.ok.tracer.HasTracerConfiguration
import ru.ok.tracer.Tracer.setUserId
import ru.ok.tracer.TracerConfiguration
import ru.ok.tracer.crash.report.CrashFreeConfiguration
import ru.ok.tracer.crash.report.CrashReportConfiguration
import ru.pomidorka.weatherapp.util.getDeviceId

class App : Application(), SingletonImageLoader.Factory, HasTracerConfiguration {
    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.DISABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
            .crossfade(true)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        val deviceId = getDeviceId(this)
        setUserId(deviceId)

        imageLoader.memoryCache?.clear()
        imageLoader.diskCache?.clear()
    }

    override val tracerConfiguration: List<TracerConfiguration>
        get() = listOf(
            CoreTracerConfiguration.build {
                // опции ядра трейсера
            },
            CrashReportConfiguration.build {
                // опции сборщика крэшей
            },
            CrashFreeConfiguration.build {
                // опции подсчета crash free
            },
        )
}