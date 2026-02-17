package ru.pomidorka.weatherapp

import android.app.Application
import ru.ok.tracer.CoreTracerConfiguration
import ru.ok.tracer.HasTracerConfiguration
import ru.ok.tracer.Tracer.setUserId
import ru.ok.tracer.TracerConfiguration
import ru.ok.tracer.crash.report.CrashFreeConfiguration
import ru.ok.tracer.crash.report.CrashReportConfiguration
import ru.pomidorka.weatherapp.util.getDeviceId

class App : Application(), HasTracerConfiguration {
    override fun onCreate() {
        super.onCreate()
        val deviceId = getDeviceId(this)
        setUserId(deviceId)
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