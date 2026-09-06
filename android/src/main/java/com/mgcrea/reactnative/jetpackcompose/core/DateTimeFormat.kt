package com.mgcrea.reactnative.jetpackcompose.core

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Formatters for the read-only picker fields.
 *
 * Formatter instances are cached per locale and reused; they are only touched from the
 * UI thread (composition), which is what makes sharing the non-thread-safe
 * [SimpleDateFormat] acceptable.
 */
internal object DateTimeFormat {
  private val utc = TimeZone.getTimeZone("UTC")
  private var cachedLocale: Locale? = null
  private var dateFormat: DateFormat? = null
  private val timeFormats = HashMap<String, SimpleDateFormat>(2)
  private val calendar: Calendar = Calendar.getInstance()

  private fun ensureLocale() {
    val locale = Locale.getDefault()
    if (locale != cachedLocale) {
      cachedLocale = locale
      dateFormat = null
      timeFormats.clear()
    }
  }

  private fun dateFormatter(): DateFormat {
    ensureLocale()
    return dateFormat ?: DateFormat.getDateInstance(DateFormat.MEDIUM, cachedLocale!!).apply {
      // Material3 date pickers work in UTC day boundaries; format the same way so the
      // field never shows the day before the one highlighted in the calendar.
      timeZone = utc
      dateFormat = this
    }
  }

  private fun timeFormatter(is24Hour: Boolean): SimpleDateFormat {
    ensureLocale()
    val pattern = if (is24Hour) "HH:mm" else "h:mm a"
    return timeFormats.getOrPut(pattern) { SimpleDateFormat(pattern, cachedLocale!!) }
  }

  /** Formats a UTC epoch-millis day as a medium-length date, or "" when null. */
  fun date(millis: Long?): String = millis?.let { dateFormatter().format(Date(it)) } ?: ""

  /** "start - end", just "start" when the end is unset, or "" when both are unset. */
  fun dateRange(startMillis: Long?, endMillis: Long?): String {
    val start = date(startMillis)
    val end = date(endMillis)
    return when {
      start.isNotEmpty() && end.isNotEmpty() -> "$start - $end"
      else -> start
    }
  }

  /** Formats minutes from midnight as a wall-clock time, or "" when null. */
  fun time(minutes: Int?, is24Hour: Boolean): String {
    if (minutes == null) return ""
    calendar.set(Calendar.HOUR_OF_DAY, minutes / 60)
    calendar.set(Calendar.MINUTE, minutes % 60)
    return timeFormatter(is24Hour).format(calendar.time)
  }

  /** "start - end" with "--" standing in for an unset end, or "" when both are unset. */
  fun timeRange(startMinutes: Int?, endMinutes: Int?, is24Hour: Boolean): String {
    val start = time(startMinutes, is24Hour)
    val end = time(endMinutes, is24Hour)
    return when {
      start.isEmpty() && end.isEmpty() -> ""
      start.isEmpty() -> "-- - $end"
      end.isEmpty() -> "$start - --"
      else -> "$start - $end"
    }
  }
}
