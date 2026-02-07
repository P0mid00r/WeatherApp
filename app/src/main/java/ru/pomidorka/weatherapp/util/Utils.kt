package ru.pomidorka.weatherapp.util

import android.content.Context
import android.provider.Settings
import java.time.DayOfWeek
import java.time.Month
import kotlin.math.round

fun <T : Number> T.toFormatTemperature(unit: String = ""): String {
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