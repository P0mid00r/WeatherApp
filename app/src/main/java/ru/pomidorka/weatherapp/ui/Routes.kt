package ru.pomidorka.weatherapp.ui

sealed class Routes(val route: String) {
    data object MainScreen : Routes("MainScreen")
    data object SelectorCityScreen : Routes("SelectorCityScreen")
}