package ru.pomidorka.weatherapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.pomidorka.weatherapp.core.api.weatherapi.entity.current.SearchData
import ru.pomidorka.weatherapp.ui.Routes

@Composable
fun SelectorCity(
    modifier: Modifier = Modifier,
    selectedCity: SearchData?,
    navController: NavController = rememberNavController(),
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .background(Color.Transparent),
            border = BorderStroke(0.dp, Color.Transparent),
            onClick = {
                navController.navigate(Routes.SelectorCityScreen.route)
            },
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(alignment = Alignment.Center),
            text = selectedCity?.name ?: "CityName",
            style = MaterialTheme.typography.titleLarge
        )
    }
}