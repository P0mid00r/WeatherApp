package ru.pomidorka.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import ru.pomidorka.weatherapp.data.WeatherToday

@Preview(showBackground = true)
@Composable
fun WeatherDayDataRowPreview(modifier: Modifier = Modifier) {
    val list = listOf(
        WeatherToday("28.04.2025", 23.3, 13.5, "Умеренный дождь", ""),
        WeatherToday("28.04.2025", 25.3, 8.6, "Местами дождь", ""),
        WeatherToday("28.04.2025", 12.6, 5.6, "Солнечно", ""),
    )

    WeatherTodayLazyRow(weathers = list)
}


@Composable
fun WeatherTodayLazyRow(
    modifier: Modifier = Modifier,
    weathers: List<WeatherToday> = emptyList()
) {
    val scrollState = rememberScrollState()

    Row(modifier.padding(15.dp, 5.dp)) {
        Column(modifier.verticalScroll(scrollState)) {
            for(weather in weathers) {
                WeatherDayDataRow(
                    weather.date,
                    weather.condition,
                    weather.temperatureDay,
                    weather.temperatureNight,
                    weather.icon
                )
                Spacer(Modifier.height(5.dp))
            }
        }
    }
}

@Composable
private fun WeatherDayDataRow(
    date: String,
    condition: String,
    temperatureMax: Double,
    temperatureMin: Double,
    icon: String
) {
    val modifier: Modifier = Modifier
    var isClicked by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                isClicked = !isClicked
            }
            .padding(16.dp),
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
            Text(
                modifier = modifier,
                text = date,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = modifier,
                text = condition,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                overflow = TextOverflow.Ellipsis,
            )
            if (isClicked) {
                Text(
                    modifier = modifier,
                    text = "Карточка открыта",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(color = MaterialTheme.colorScheme.onPrimaryContainer, text = "$temperatureMax°C / $temperatureMin°C")
        }
    }
}