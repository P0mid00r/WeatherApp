package ru.pomidorka.weatherapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SimpleLoadingIndicator(
    modifier: Modifier = Modifier,
    size: DpSize,
    color: Color = LoadingIndicatorDefaults.indicatorColor
) {
    Box(modifier = modifier) {
        LoadingIndicator(
            color = color,
            modifier = modifier
                .size(size)
                .align(Alignment.Center)
        )
    }
}