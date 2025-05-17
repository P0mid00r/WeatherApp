package ru.pomidorka.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.pomidorka.weatherapp.core.weatherapi.openmeteo.OpenMeteoApiController
import ru.pomidorka.weatherapp.data.WeatherToday
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.ui.components.CurrentTemperatureView
import ru.pomidorka.weatherapp.ui.components.SelectorCity
import ru.pomidorka.weatherapp.ui.components.WeatherChart
import ru.pomidorka.weatherapp.ui.components.WeatherTodayLazyRow
import ru.pomidorka.weatherapp.util.Date
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            var data = viewModel.currentWeather?.current

            SelectorCity(
                modifier = modifier.padding(25.dp, 0.dp),
                selectedCity = viewModel.selectedCity,
                navController = navController,
            )
            CurrentTemperatureView(data?.temp_c ?: 0.0, 0,0)
        }

        Column(modifier.padding(15.dp, 5.dp, 15.dp, 0.dp)) {
            WeatherChart(viewModel.values, modifier)
        }

        viewModel.forecast?.let {
            val list: MutableList<WeatherToday> = arrayListOf()
            for(day in viewModel.forecast!!.forecast.forecastday) {
                list.add(
                    WeatherToday(
                        Date.getNameDayOfWeek(LocalDate.parse(day.date).dayOfWeek),
                        day.day.maxtemp_c,
                        day.day.mintemp_c,
                        day.day.condition.text,
                        day.day.condition.icon,
                    )
                )
            }

            WeatherTodayLazyRow(weathers = list)
        }
    }
}