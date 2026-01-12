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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.WeatherForDay
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.minutely.Minutely15
import ru.pomidorka.weatherapp.data.WeatherViewModel
import ru.pomidorka.weatherapp.util.getDayOfWeekName
import ru.pomidorka.weatherapp.util.getMonthName
import ru.pomidorka.weatherapp.util.toRoundedFormatTemperature
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
fun WeatherForDayLazyRows(
    modifier: Modifier = Modifier,
    weathers: List<WeatherForDay> = emptyList(),
    viewModel: WeatherViewModel = viewModel()
) {
    Column(modifier.padding(15.dp, 5.dp)) {
        for(weather in weathers) {
            WeatherForDayRow(
                onClickLoadDetailRow = {
                    viewModel.getWeathersOfDates(
                        weather.date,
                        weather.date
                    )
                },
                weatherForDay = weather,
            )
            Spacer(Modifier.height(5.dp))
        }
    }
}

@Composable
private fun WeatherForDayRow(
    onClickLoadDetailRow: suspend () -> Minutely15?,
    weatherForDay: WeatherForDay,
) {
    val modifier: Modifier = Modifier
    val scope = rememberCoroutineScope()
    var isOpened by remember { mutableStateOf(false) }
    val temperatureList = remember { mutableListOf<Double>() }
    val timeList = remember { mutableListOf<String>() }
    var isLoadingChart by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.large
            )
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                isOpened = !isOpened
            }
            .padding(16.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!weatherForDay.icon.isBlank()) {
                AsyncImage(
                    modifier = modifier.size(56.dp),
                    model = weatherForDay.icon,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = modifier.weight(1f)) {
                val currentDate = LocalDate.now()
                val date = LocalDate.parse(weatherForDay.date)

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
                    text = weatherForDay.condition,
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
                    text = "${weatherForDay.temperatureMax.toRoundedFormatTemperature()} / ${weatherForDay.temperatureMin.toRoundedFormatTemperature()}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        AnimatedVisibility(isOpened) {
            LaunchedEffect(Unit) {
                if (isLoadingChart) {
                    scope.launch(Dispatchers.IO) {
                        val minutely15 = onClickLoadDetailRow()

                        minutely15?.let {
                            for (i in 0..it.time.count() - 1) {
                                val dateTime = LocalDateTime.parse(it.time[i])

                                temperatureList.add(listOf(it.temperature2m[i], it.apparentTemperature[i]).average())
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
                        Box(Modifier
                            .fillMaxSize()
                            .padding(24.dp)) {
                            SimpleLoadingIndicator(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                size = DpSize(70.dp, 70.dp),
                                modifier = modifier.align(Alignment.Center)
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