package ru.pomidorka.weatherapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ru.pomidorka.weatherapp.core.api.openmeteo.entity.search.City

@Preview
@Composable
private fun SelectorCityPreview() {
    SelectorCity(
        Modifier,
        null,
        {}
    )
}

@Composable
fun SelectorCity(
    modifier: Modifier = Modifier,
    selectedCity: City?,
    onButtonClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onButtonClick
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(alignment = Alignment.Center),
            text = selectedCity?.name ?: "CityName",
            style = MaterialTheme.typography.titleLarge
        )
    }
}