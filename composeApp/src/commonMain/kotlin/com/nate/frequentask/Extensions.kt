package com.nate.frequentask

import androidx.compose.runtime.Composable
import frequentaskmulti.composeapp.generated.resources.Res
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import frequentaskmulti.composeapp.generated.resources.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.stringResource

fun Long.isDue(): Boolean {
    return todayDate() >= date()
}

private fun todayDate(): LocalDate {
    return Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
}

private fun Long.date(): LocalDate {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
}

fun Long.isLate(): Boolean {
    return todayDate() > date()
}

@Composable
fun Long.displayDate(): String {
    return date().displayDate()
}

private val longDateFormat = LocalDate.Format {
    dayOfMonth()
    char('/')
    monthNumber()
    char('/')
    year()
}
private val shortDateFormat = LocalDate.Format {
    dayOfMonth()
    char('/')
    monthNumber()
}

@Composable
private fun LocalDate.displayDate(): String {
    return when (format(longDateFormat)) {
        today() -> stringResource(Res.string.today)
        tomorrow() -> stringResource(Res.string.tomorrow)
        yesterday() -> stringResource(Res.string.yesterday)
        else -> format(shortDateFormat)
    }
}

fun yesterday(): String {
    return todayDate().minus(1, DateTimeUnit.DAY).format(longDateFormat)
}

fun tomorrow(): String {
    return todayDate().plus(1, DateTimeUnit.DAY).format(longDateFormat)
}

fun today(): String {
    return todayDate().format(longDateFormat)
}

private fun midnightToday(): LocalDateTime {
    todayDate().run {
        val formatPattern = "dd/MM/yyyy'T'HH:mm:ss[.SSS]"

        @OptIn(FormatStringsInDatetimeFormats::class) val dateTimeFormat = LocalDateTime.Format {
            byUnicodePattern(formatPattern)
        }
        return dateTimeFormat.parse("${format(longDateFormat)}T00:00:00")
    }
}

private fun Long.isInthePast(): Boolean {
    return this < midnightToday().toInstant(kotlinx.datetime.TimeZone.currentSystemDefault())
        .toEpochMilliseconds()
}

fun Long.displayGroupVal(): String {
    return if (isInthePast()) "past" else date().format(longDateFormat)
}

@Composable
fun String.displayGroupTitle(): String {
    return when (this) {
        "past" -> stringResource(Res.string.overdue)
        yesterday() -> stringResource(Res.string.yesterday)
        today() -> stringResource(Res.string.today)
        tomorrow() -> stringResource(Res.string.tomorrow)
        else -> this.dropLastWhile { it != '/' }.dropLast(1)
    }
}

fun now(): Long = Clock.System.now().toEpochMilliseconds()

