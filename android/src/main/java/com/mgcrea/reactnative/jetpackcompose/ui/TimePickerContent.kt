package com.mgcrea.reactnative.jetpackcompose.ui

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Dial/keyboard time picker body shared by the single and range time pickers:
 * an optional mode-toggle row followed by [TimePicker] or [TimeInput].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimePickerContent(
  state: TimePickerState,
  showDial: Boolean,
  showModeToggle: Boolean,
  onToggleMode: () -> Unit,
  toggleRowPadding: PaddingValues = PaddingValues(bottom = 16.dp),
) {
  if (showModeToggle) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(toggleRowPadding),
      horizontalArrangement = Arrangement.End,
    ) {
      IconButton(onClick = onToggleMode) {
        Icon(
          imageVector = if (showDial) Icons.Default.Edit else Icons.Default.DateRange,
          contentDescription = if (showDial) "Switch to keyboard input" else "Switch to dial",
        )
      }
    }
  }
  if (showDial) TimePicker(state = state) else TimeInput(state = state)
}

/**
 * The system 24-hour preference, read once per configuration rather than on every
 * recomposition (it is a settings-provider lookup).
 */
@Composable
internal fun rememberSystem24Hour(): Boolean {
  val context = LocalContext.current
  val configuration = LocalConfiguration.current
  return remember(configuration) { DateFormat.is24HourFormat(context) }
}

/** Minutes from midnight for a [TimePickerState]. */
@OptIn(ExperimentalMaterial3Api::class)
internal val TimePickerState.minutesFromMidnight: Int
  get() = hour * 60 + minute

/** Creates a [TimePickerState] positioned at [minutes] from midnight (midnight when null). */
@OptIn(ExperimentalMaterial3Api::class)
internal fun timePickerState(minutes: Int?, is24Hour: Boolean): TimePickerState {
  val m = minutes ?: 0
  return TimePickerState(initialHour = m / 60, initialMinute = m % 60, is24Hour = is24Hour)
}
