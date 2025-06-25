package ru.pomidorka.weatherapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SimpleLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = LoadingIndicatorDefaults.indicatorColor
) {
//    val scope = rememberCoroutineScope()
//
//    var progress by remember { mutableFloatStateOf(0.0f) }
//    val animatedProgress by animateFloatAsState(
//        targetValue = progress,
//        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
//    )
//
//    LaunchedEffect(Unit) {
//        scope.launch {
//            while (progress != 100f) {
//                progress += 0.01f
//                delay(30)
//            }
//        }
//    }

    Box(modifier = modifier) {
        LoadingIndicator(
            color = color,
            modifier = modifier.align(Alignment.Center)
        )
//        CircularWavyProgressIndicator(
//            modifier = modifier.align(Alignment.Center),
//            trackColor = MaterialTheme.colorScheme.primaryContainer,
//            color = MaterialTheme.colorScheme.primary,
//        )
//        CircularProgressIndicator(
//            trackColor = MaterialTheme.colorScheme.primary,
//            color = MaterialTheme.colorScheme.primaryContainer,
//            modifier = modifier.align(Alignment.Center),
//            strokeWidth = 8.dp
//        )
    }
}