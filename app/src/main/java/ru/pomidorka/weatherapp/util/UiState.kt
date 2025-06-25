package ru.pomidorka.weatherapp.util

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    object Loaded : UiState<Nothing>()
    object Error : UiState<Nothing>()
}