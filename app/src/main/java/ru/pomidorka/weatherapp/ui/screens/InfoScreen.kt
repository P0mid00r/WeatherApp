package ru.pomidorka.weatherapp.ui.screens

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.pomidorka.weatherapp.BuildConfig
import ru.pomidorka.weatherapp.R
import ru.pomidorka.weatherapp.util.getDeviceId

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun InfoScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.background(MaterialTheme.colorScheme.primary)
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
        }

        Column(modifier = modifier.padding(24.dp, 0.dp)) {
            Image(
                painter = painterResource(R.drawable.logo_no_background),
                contentDescription = null
            )
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current

            Text("Версия: ${BuildConfig.VERSION_NAME}")
            Text("Android: ${Build.VERSION.RELEASE}")
            Text("DeviceID: ${getDeviceId(context)}")
        }
    }
}