package ru.pomidorka.weatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.ui.screens.MainScreen
import ru.pomidorka.weatherapp.ui.screens.SelectorCityScreen
import ru.pomidorka.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    private companion object {
        lateinit var viewModel: WeatherViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            viewModel = WeatherViewModel(applicationContext)
            LaunchedEffect(Unit) {
                viewModel.loadWeatherInfo()
                viewModel.setEnabledUpdaterWeatherInfo(false)
            }

            WeatherAppTheme {
                NavHost(navController = navController, startDestination = Routes.MainScreen.route) {
                    composable(Routes.MainScreen.route) {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            MainScreen(viewModel = viewModel, navController = navController)
                        }
                    }
                    composable(Routes.SelectorCityScreen.route) {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            SelectorCityScreen(viewModel = viewModel, navController = navController)
                        }
                    }
                }
            }
        }
    }
}