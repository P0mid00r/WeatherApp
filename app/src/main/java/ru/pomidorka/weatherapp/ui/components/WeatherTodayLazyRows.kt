package ru.pomidorka.weatherapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import ru.pomidorka.weatherapp.core.api.weatherapi.entity.WeatherToday
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.util.getDayOfWeekName
import ru.pomidorka.weatherapp.util.getMonthName
import ru.pomidorka.weatherapp.util.toFormatTemperature
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//@Preview(showBackground = true)
//@Composable
//private fun WeatherDayDataRowPreview(modifier: Modifier = Modifier) {
//    val list = listOf(
//        WeatherToday("28.04.2025", 23.3, 13.5, "Умеренный дождь", ""),
//        WeatherToday("28.04.2025", 25.3, 8.6, "Местами дождь", ""),
//        WeatherToday("28.04.2025", 12.6, 5.6, "Солнечно", ""),
//    )
//
//    WeatherTodayLazyRows(weathers = list)
//}

@Composable
fun WeatherTodayLazyRows(
    modifier: Modifier = Modifier,
    weathers: List<WeatherToday> = emptyList(),
    viewModel: WeatherViewModel = viewModel()
) {
    Row(modifier.padding(15.dp, 5.dp)) {
        Column {
            for(weather in weathers) {
                WeatherDayDataRow(
                    viewModel,
                    weather.date,
                    weather.condition,
                    weather.temperatureDay,
                    weather.temperatureNight,
                    weather.icon,
                )
                Spacer(Modifier.height(5.dp))
            }
        }
    }
}

@Composable
private fun WeatherDayDataRow(
    viewModel: WeatherViewModel = viewModel(),
    date: String,
    condition: String,
    temperatureMax: Double,
    temperatureMin: Double,
    icon: String,
) {
    val modifier: Modifier = Modifier
    val scope = rememberCoroutineScope()
    var isClicked by remember { mutableStateOf(false) }
    val temperatureList = remember { mutableListOf<Double>() }
    val timeList = remember { mutableListOf<String>() }
    var isLoadingChart by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                isClicked = !isClicked
            }
            .padding(16.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = modifier.size(56.dp),
                model = "https:$icon",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(modifier = modifier.weight(1f)) {
                val currentDate = LocalDate.now()
                val date = LocalDate.parse(date)

                val text = if (currentDate.plusDays(3) <= date) {
                    "${date.dayOfMonth} ${date.month.getMonthName()}"
                } else {
                    date.dayOfWeek.getDayOfWeekName()
                }

                Text(
                    modifier = modifier,
                    text = text,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = modifier,
                    text = condition,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    text = "${temperatureMax.toFormatTemperature()} / ${temperatureMin.toFormatTemperature()}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        AnimatedVisibility(isClicked) {
            LaunchedEffect(Unit) {
                if (isLoadingChart) {
                    scope.launch {
                        val minutely15 = viewModel.getWeathersOfDates(
                            startDate = date,
                            endDate = date
                        )

                        minutely15?.let {
                            for (i in 0..it.time.count() - 1) {
                                val dateTime = LocalDateTime.parse(it.time[i])

                                temperatureList.add(listOf(it.temperature_2m[i], it.apparent_temperature[i]).average())
                                val dateTimeString = dateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
                                timeList.add("$dateTimeString ${"%02d".format(dateTime.hour)}:${"%02d".format(dateTime.minute)}")
                            }
                        }

                        isLoadingChart = false
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(0.dp, 4.dp, 0.dp, 0.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                thickness = 4.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            AnimatedContent(targetState = isLoadingChart) {
                when(it) {
                    true -> {
                        Box(Modifier.fillMaxSize().padding(24.dp)) {
                            SimpleLoadingIndicator(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = modifier.size(70.dp).align(Alignment.Center)
                            )
                        }
                    }
                    false -> {
                        WeatherChart(
                            title = "Температура в течение дня",
                            temperatureValues = temperatureList,
                            timeValues = timeList,
                            modifier = modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}