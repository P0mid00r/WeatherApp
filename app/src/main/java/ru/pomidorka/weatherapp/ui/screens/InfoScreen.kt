package ru.pomidorka.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.pomidorka.weatherapp.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun InfoScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CenterAlignedTopAppBar(
            modifier = modifier.fillMaxWidth(),
            title = { Text("Информация") },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Column(modifier = modifier.padding(24.dp, 0.dp)) {
            Text(
                "Данное приложение создано для удобного просмотра погоды на каждый день. В приложении можно выбирать любой город, а также сохранять его для дальнейнего просмотра. В случае возникнования вопросов, обращайтесь по адресу: sergebelousov2018@gmail.com",
                fontSize = 20.sp
            )
        }

        Column(
            modifier = modifier.fillMaxWidth().padding(0.dp, 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Создатель: Белоусов Сергей Сергеевич")
            Text("Версия: ${BuildConfig.VERSION_NAME}")
        }
    }
}