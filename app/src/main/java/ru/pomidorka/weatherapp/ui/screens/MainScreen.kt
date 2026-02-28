package ru.pomidorka.weatherapp.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.skipDays
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.toWeatherForDayList
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.ui.Routes
import ru.pomidorka.weatherapp.ui.components.CurrentTemperatureView
import ru.pomidorka.weatherapp.ui.components.SelectorCity
import ru.pomidorka.weatherapp.ui.components.SimpleLoadingIndicator
import ru.pomidorka.weatherapp.ui.components.WeatherChart
import ru.pomidorka.weatherapp.ui.components.WeatherForDayLazyRows

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        viewModel = viewModel(),
        navController = rememberNavController()
    )
}

@Composable
fun MainScreen(
    viewModel: WeatherViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val state by viewModel.mainScreenState.collectAsState()
    val currentWeather = state.currentWeather

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .clip(
                    RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomEndPercent = 12,
                        bottomStartPercent = 12
                    )
                )
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            SelectorCity(
                modifier = modifier
                    .statusBarsPadding()
                    .padding(25.dp, 0.dp),
                selectedCity = state.selectedCity,
                onButtonClick = {
                    navController.navigate(Routes.SelectorCityScreen)
                }
            )
            AnimatedContent(targetState = state.isLoadingData) {
                when(it) {
                    true -> SimpleLoadingIndicator(
                        modifier = modifier,
                        size = DpSize(70.dp, 70.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    false -> CurrentTemperatureView(
                        currentTemperature = currentWeather?.temperature ?: 0.0f,
                        temperatureMax = currentWeather?.temperatureMax ?: 0.0f,
                        temperatureMin = currentWeather?.temperatureMin ?: 0.0f,
                        condition = currentWeather?.condition ?: "null"
                    )
                }
            }
        }

        AnimatedContent(targetState = state.isLoadingData) {
            if (it) {
                Box(
                    modifier
                        .padding(15.dp, 5.dp, 15.dp, 0.dp)
                        .fillMaxSize()
                ) {
                    SimpleLoadingIndicator(
                        modifier = modifier.align(Alignment.Center),
                        size = DpSize(100.dp, 100.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            } else {
                Column(Modifier) {
                    Column(
                        modifier
                            .padding(15.dp, 5.dp, 15.dp, 0.dp)
                            .clip(shape = MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        if (state.temperatureValues.count() > 1) {
                            WeatherChart(
                                title = "Температура на 24 часа",
                                temperatureValues = state.temperatureValues,
                                timeValues = state.timeValues,
                                modifier = modifier.padding(24.dp)
                            )
                        }
                    }

                    state.weather?.let {
                        WeatherForDayLazyRows(
                            viewModel = viewModel,
                            weathers = it.daily.skipDays(1).toWeatherForDayList()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                    )
                }
            }
        }
    }
}