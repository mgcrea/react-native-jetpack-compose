package com.mgcrea.reactnative.jetpackcompose.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates

/**
 * Builds the [SelectableDates] constraint for the date pickers from inclusive
 * min/max UTC epoch millis and the selectable year range.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal fun selectableDates(minMillis: Long?, maxMillis: Long?, years: IntRange): SelectableDates =
  object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
      (minMillis == null || utcTimeMillis >= minMillis) && (maxMillis == null || utcTimeMillis <= maxMillis)

    override fun isSelectableYear(year: Int): Boolean = year in years
  }

/** Default year range when the JS side sets none. */
internal val DEFAULT_YEAR_RANGE: IntRange = 1900..2100
