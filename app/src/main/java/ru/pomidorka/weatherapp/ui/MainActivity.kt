package ru.pomidorka.weatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.pomidorka.weatherapp.ad.OpenAppAd
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.ui.dialogs.NetworkNotAvailableAlertDialog
import ru.pomidorka.weatherapp.ui.theme.WeatherAppTheme
import ru.pomidorka.weatherapp.util.NetworkState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        OpenAppAd.load(this)

        setContent {
            val viewModel = viewModel { WeatherViewModel(applicationContext) }
            val networkState by viewModel.networkChecker.networkState.collectAsState()
            var showAlertDialog by remember { mutableStateOf(false) }

            showAlertDialog = when(networkState) {
                NetworkState.Connected -> false
                NetworkState.Disconnected -> true
            }

            WeatherAppTheme {
                if (showAlertDialog) {
                    NetworkNotAvailableAlertDialog()
                }

                AppNavigation(viewModel)
            }
        }
    }
}