package ru.pomidorka.weatherapp.ui.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ru.pomidorka.weatherapp.util.toRoundedFormatTemperature

@Composable
fun WeatherChart(
    title: String,
    temperatureValues: List<Double>,
    timeValues: List<String>,
    modifier: Modifier = Modifier
) {
    val popupProperties = PopupProperties(
        enabled = true,
        animationSpec = tween(300),
        duration = 2000L,
        textStyle = MaterialTheme.typography.labelSmall,
        containerColor = Color.White,
        cornerRadius = 8.dp,
        contentHorizontalPadding = 4.dp,
        contentVerticalPadding = 2.dp,
        contentBuilder = { _, valueIndex, value ->
            value.toRoundedFormatTemperature().plus("\n${timeValues[valueIndex]}")
        }
    )

    val onPrimaryContainerColor = MaterialTheme.colorScheme.onPrimaryContainer

    LineChart(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        data = remember {
            listOf(
                Line(
                    label = title,
                    values = temperatureValues,
                    color = SolidColor(onPrimaryContainerColor),
                    firstGradientFillColor = onPrimaryContainerColor.copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 4.dp),
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        labelHelperProperties = LabelHelperProperties(
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
                color = onPrimaryContainerColor
            )
        ),
        gridProperties = GridProperties(enabled = false),
        maxValue = temperatureValues.max() + 5,
        minValue = temperatureValues.min() - 5,
        popupProperties = popupProperties,
        indicatorProperties = HorizontalIndicatorProperties(
            textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
            padding = 16.dp,
            contentBuilder = { indicator ->
                indicator.toRoundedFormatTemperature()
            },
        ),
    )
}
