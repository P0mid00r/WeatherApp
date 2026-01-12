package ru.pomidorka.weatherapp.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import ru.pomidorka.weatherapp.ui.theme.WeatherAppTheme
import java.time.DayOfWeek
import java.time.Month
import kotlin.math.round

@Composable
fun rememberCurrentLocation(): MutableState<Location?> {
    val context = LocalContext.current
    val activity = context as Activity
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val locationState = remember { mutableStateOf<Location?>(null) }
    val showDialog = remember { mutableStateOf(false) }
    val permissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
    }

    // Разрешения и GPS-проверка
    LaunchedEffect(Unit) {
        if (!permissionGranted.value) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showDialog.value = true
            } else {
                requestSingleLocationUpdate(locationManager, locationState)
            }
        }
    }

    // GPS диалог
    if (showDialog.value) {
        WeatherAppTheme {

            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("GPS выключен") },
                text = { Text("Для работы функции включите GPS.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog.value = false
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }) {
                        Text("Открыть настройки")
                    }
                },
//                dismissButton = {
//                    TextButton(onClick = { showDialog.value = false }) {
//                        Text("Отмена")
//                    }
//                },
                properties = DialogProperties(false, false)
            )
        }
    }

    return locationState
}

fun requestSingleLocationUpdate(
    locationManager: LocationManager,
    locationState: MutableState<Location?>
) {
    try {
//        locationManager.requestSingleUpdate(
//            LocationManager.GPS_PROVIDER,
//            object : LocationListener {
//                override fun onLocationChanged(loc: Location) {
//                    locationState.value = loc
//                }
//            },
//            null
//        )
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}

fun checkGpsAndRequestLocation(
    context: Context,
    locationManager: LocationManager,
    locationState: MutableState<Location?>
) {
    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    if (!isGpsEnabled) {
        AlertDialog.Builder(context)
            .setTitle("GPS выключен")
            .setMessage("Включите GPS для определения местоположения.")
            .setPositiveButton("Открыть настройки") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            .setNegativeButton("Отмена", null)
            .show()
    } else {
//        try {
//            locationManager.requestSingleUpdate(
//                LocationManager.GPS_PROVIDER,
//                object : LocationListener {
//                    override fun onLocationChanged(loc: Location) {
//                        locationState.value = loc
//                    }
//                },
//                null
//            )
//        } catch (e: SecurityException) {
//            e.printStackTrace()
//        }
    }
}

fun <T : Number> T.toFormatTemperature1(unit: String = ""): String {
    val value = this.toFloat()
    val prefix = when {
        value > 0 -> "+"
        else -> ""
    }

    return "$prefix%.1f°$unit".format(value).replace(',', '.')
}

fun <T : Number> T.toRoundedFormatTemperature(unit: String = ""): String {
    val value = round(this.toFloat()).toInt()
    val prefix = when {
        value > 0 -> "+"
        else -> ""
    }

    return "$prefix$value°$unit"
}

fun DayOfWeek.getDayOfWeekName(): String {
    return when(this) {
        DayOfWeek.MONDAY -> "Понедельник"
        DayOfWeek.TUESDAY -> "Вторник"
        DayOfWeek.WEDNESDAY -> "Среда"
        DayOfWeek.THURSDAY -> "Четверг"
        DayOfWeek.FRIDAY -> "Пятница"
        DayOfWeek.SATURDAY -> "Суббота"
        DayOfWeek.SUNDAY -> "Воскресенье"
    }
}

fun Month.getMonthName(): String {
    return when(this) {
        Month.JANUARY -> "Января"
        Month.FEBRUARY -> "Февраля"
        Month.MARCH -> "Марта"
        Month.APRIL -> "Апреля"
        Month.MAY -> "Мая"
        Month.JUNE -> "Июня"
        Month.JULY -> "Июля"
        Month.AUGUST -> "Августа"
        Month.SEPTEMBER -> "Сентября"
        Month.OCTOBER -> "Октября"
        Month.NOVEMBER -> "Ноября"
        Month.DECEMBER -> "Декабря"
    }
}

fun getDeviceId(context: Context): String {
    return Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}