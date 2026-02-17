package ru.pomidorka.weatherapp.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NetworkNotAvailableAlertDialog(modifier: Modifier = Modifier) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text("Ошибка сети")
        },
        text = {
            Text("Для работы приложения требуется подключение к интернету!")
        },
        onDismissRequest = {

        },
        confirmButton = {

        }
    )
}