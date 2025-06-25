package ru.pomidorka.weatherapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ru.pomidorka.weatherapp.util.toFormatTemperature

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CurrentTemperatureView(
    currentTemperature: Double,
    temperatureMax: Double,
    temperatureMin: Double,
    condition: String,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            text = currentTemperature.toFormatTemperature(),
            style = MaterialTheme.typography.displayMedium,
        )
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyLarge,
            text = condition + "\n${temperatureMax.toFormatTemperature()} / ${temperatureMin.toFormatTemperature()}",
            textAlign = TextAlign.Center
        )
    }
}