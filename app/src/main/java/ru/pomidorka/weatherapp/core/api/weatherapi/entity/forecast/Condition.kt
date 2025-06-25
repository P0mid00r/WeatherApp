package ru.pomidorka.weatherapp.core.api.weatherapi.entity.forecast

data class Condition(
    val code: Int,
    val icon: String,
    val text: String
)