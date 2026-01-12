package ru.pomidorka.weatherapp.ui

import kotlinx.serialization.Serializable

sealed class Routes() {
    @Serializable
    data object MainScreen : Routes()
    @Serializable
    data object SelectorCityScreen : Routes()
    @Serializable
    data object InfoScreen : Routes()
}