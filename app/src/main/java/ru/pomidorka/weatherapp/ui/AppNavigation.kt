package ru.pomidorka.weatherapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.ui.screens.InfoScreen
import ru.pomidorka.weatherapp.ui.screens.MainScreen
import ru.pomidorka.weatherapp.ui.screens.SelectorCityScreen

@Composable
fun AppNavigation(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.MainScreen
    ) {
        composable<Routes.MainScreen> {
            MainScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable<Routes.SelectorCityScreen> {
            SelectorCityScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable<Routes.InfoScreen> {
            InfoScreen()
        }
    }
}