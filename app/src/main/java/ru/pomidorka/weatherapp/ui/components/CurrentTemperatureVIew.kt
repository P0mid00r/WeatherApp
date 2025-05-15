package ru.pomidorka.weatherapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrentTemperatureView(
    currentTemperature: Double,
    temperatureDay: Int,
    temperatureNight: Int,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            text = "$currentTemperature°С",
            fontSize = 35.sp,
        )
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            text = "Ясно $temperatureDay°/$temperatureNight°",
        )
    }
}