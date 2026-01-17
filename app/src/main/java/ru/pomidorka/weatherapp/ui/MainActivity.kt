package ru.pomidorka.weatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.pomidorka.weatherapp.ad.OpenAppAd
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        OpenAppAd.load(this)

        setContent {
            val viewModel = viewModel { WeatherViewModel(applicationContext) }

            WeatherAppTheme {
                AppNavigation(viewModel)
            }
        }
    }
}