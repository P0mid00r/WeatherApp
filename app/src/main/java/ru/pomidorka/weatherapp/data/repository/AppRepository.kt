package ru.pomidorka.weatherapp.data.repository

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import ru.pomidorka.weatherapp.data.repository.migrations.migrateToLastVersion

class AppRepository(context: Context) {
    private val jsonBuilder = Json {
        encodeDefaults = true
    }
    private val scope = CoroutineScope(Dispatchers.IO)
    private val appSharedPreferences = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)

    private val _appSettingsState = MutableStateFlow(load())
    val appSettingsState = _appSettingsState.asStateFlow()

    init {
        scope.launch {
            appSettingsState.collect {
                save(it)
            }
        }
    }

    private fun load(): AppSettings {
        val json = appSharedPreferences
            .getString(APP_SETTINGS, null) ?: return AppSettings()

        val jsonElement = jsonBuilder
            .parseToJsonElement(json)
            .migrateToLastVersion()

        return jsonBuilder.decodeFromJsonElement<AppSettings>(jsonElement)
    }

    fun update(func: (AppSettings) -> AppSettings) {
        val newAppSettings = func(appSettingsState.value)
        if (newAppSettings != appSettingsState.value) {
            _appSettingsState.update { newAppSettings }
        }
    }

    private fun save(appSettings: AppSettings) {
        val json = jsonBuilder.encodeToString(appSettings)
        appSharedPreferences.edit {
            putString(APP_SETTINGS, json)
        }
    }

    private fun clear() = appSharedPreferences.edit { clear() }

    companion object {
        private const val APP_SETTINGS = "APP_SETTINGS"
    }
}