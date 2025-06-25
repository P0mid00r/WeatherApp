package ru.pomidorka.weatherapp.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.pomidorka.weatherapp.core.api.weatherapi.entity.WeatherToday
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.ui.components.SimpleLoadingIndicator
import ru.pomidorka.weatherapp.ui.components.CurrentTemperatureView
import ru.pomidorka.weatherapp.ui.components.SelectorCity
import ru.pomidorka.weatherapp.ui.components.WeatherChart
import ru.pomidorka.weatherapp.ui.components.WeatherTodayLazyRows
import java.time.LocalDate

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(modifier: Modifier = Modifier) {
    MainScreen()
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxSize()) {
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
            var data = viewModel.currentWeather?.current

            SelectorCity(
                modifier = modifier.padding(25.dp, 0.dp),
                selectedCity = viewModel.selectedCity,
                navController = navController,
            )
            AnimatedContent(targetState = viewModel.isLoadingData) {
                when(it) {
                    true -> SimpleLoadingIndicator(
                        modifier = modifier.size(70.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    false -> CurrentTemperatureView(
                        currentTemperature = data?.temp_c ?: 0.0,
                        temperatureMax = viewModel.forecast?.forecast?.forecastday[0]?.day?.maxtemp_c ?: 0.0,
                        temperatureMin = viewModel.forecast?.forecast?.forecastday[0]?.day?.mintemp_c ?: 0.0,
                        data?.condition?.text ?: "Ясно"
                    )
                }
            }
        }

        AnimatedContent(targetState = viewModel.isLoadingData) {
            if (it) {
                Box(
                    modifier
                        .padding(15.dp, 5.dp, 15.dp, 0.dp)
                        .fillMaxSize()
                ) {
                    SimpleLoadingIndicator(
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = modifier.size(100.dp).align(Alignment.Center)
                    )
                }
            } else {
                Column(Modifier.verticalScroll(scrollState)) {
                    Column(
                        modifier
                            .padding(15.dp, 5.dp, 15.dp, 0.dp)
                            .clip(shape = MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        if (viewModel.temperatureValues.count() > 1) {
                            WeatherChart(
                                title = "Температура 24 часа",
                                temperatureValues = viewModel.temperatureValues,
                                timeValues = viewModel.timeValues,
                                modifier = modifier.padding(24.dp)
                            )
                        }
                    }

                    viewModel.forecast?.let {
                        val list: MutableList<WeatherToday> = arrayListOf()
                        for (day in viewModel.forecast!!.forecast.forecastday) {
                            list.add(
                                WeatherToday(
                                    day.date,
                                    day.day.maxtemp_c,
                                    day.day.mintemp_c,
                                    day.day.condition.text,
                                    day.day.condition.icon,
                                )
                            )
                        }

                        WeatherTodayLazyRows(
                            viewModel = viewModel,
                            weathers = list
                        )
                    }
                }
            }
        }
    }
}