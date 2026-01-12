package ru.pomidorka.weatherapp.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.pomidorka.weatherapp.BuildConfig
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.OpenMeteoData
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.City

@Serializable
data class AppData(
    val versionData: String = BuildConfig.VERSION_NAME,
    val selectedCity: City? = null,
    val favoritesCity: List<City> = emptyList()
)

//data class AppCache(
//    val weathers: List<OpenMeteoData> = emptyList(),
//    val searchedCities: HashMap<String, List<City>> = hashMapOf()
//)

class AppRepository(context: Context) {
    private val appSharedPreferences = context.getSharedPreferences(APP_DATA, MODE_PRIVATE)

    var appData = load()
//
//    private val _appDataState = MutableStateFlow(AppData())
//    val appDataState = _appDataState.asStateFlow()

    private fun load(): AppData {
        val json = appSharedPreferences.getString(
            APP_DATA,
            Json.encodeToString(AppData())
        ) ?: return AppData()
        return Json.decodeFromString<AppData>(json)
    }

    fun save() {
        val json = Json.encodeToString(appData)
        appSharedPreferences.edit {
            putString(APP_DATA, json)
        }
    }

    private fun clear() = appSharedPreferences.edit { clear() }

    companion object {
        private const val APP_SETTINGS = "APP_SETTINGS"
        private const val APP_DATA = "APP_DATA"
    }
}