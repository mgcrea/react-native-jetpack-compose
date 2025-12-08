package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.events.CancelEvent
import com.mgcrea.reactnative.jetpackcompose.events.TimeRangeChangeEvent
import com.mgcrea.reactnative.jetpackcompose.events.TimeRangeConfirmEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
internal class TimeRangePickerView(reactContext: ThemedReactContext) :
  InlineComposeView(reactContext, TAG) {

  companion object {
    private const val TAG = "TimeRangePickerView"
  }

  // State backing for Compose
  private val _startTimeMinutes = mutableStateOf<Int?>(null)
  private val _endTimeMinutes = mutableStateOf<Int?>(null)
  private val _is24Hour = mutableStateOf<Boolean?>(null)
  private val _initialDisplayMode = mutableStateOf("picker")
  private val _showModeToggle = mutableStateOf(true)
  private val _confirmLabel = mutableStateOf<String?>(null)
  private val _cancelLabel = mutableStateOf<String?>(null)
  private val _titleText = mutableStateOf<String?>(null)

  // State for OutlinedTextField
  private val _label = mutableStateOf<String?>(null)
  private val _placeholder = mutableStateOf<String?>(null)
  private val _disabled = mutableStateOf(false)
  private var _showDialog by mutableStateOf(false)

  // Property setters called by ViewManager
  fun setStartTimeMinutes(value: Int?) {
    _startTimeMinutes.value = value
  }

  fun setEndTimeMinutes(value: Int?) {
    _endTimeMinutes.value = value
  }

  fun setIs24Hour(value: Boolean?) {
    _is24Hour.value = value
  }

  fun setInitialDisplayMode(value: String?) {
    _initialDisplayMode.value = value ?: "picker"
  }

  fun setShowModeToggle(value: Boolean) {
    _showModeToggle.value = value
  }

  fun setConfirmLabel(value: String?) {
    _confirmLabel.value = value
  }

  fun setCancelLabel(value: String?) {
    _cancelLabel.value = value
  }

  fun setTitleText(value: String?) {
    _titleText.value = value
  }

  fun setLabel(value: String?) {
    _label.value = value
  }

  fun setPlaceholder(value: String?) {
    _placeholder.value = value
  }

  fun setDisabled(value: Boolean) {
    _disabled.value = value
  }

  private fun formatTime(minutes: Int?, is24Hour: Boolean): String {
    if (minutes == null) return ""
    val hours = minutes / 60
    val mins = minutes % 60
    val calendar = Calendar.getInstance().apply {
      set(Calendar.HOUR_OF_DAY, hours)
      set(Calendar.MINUTE, mins)
    }
    val pattern = if (is24Hour) "HH:mm" else "h:mm a"
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(calendar.time)
  }

  private fun formatTimeRange(startMinutes: Int?, endMinutes: Int?, is24Hour: Boolean): String {
    val start = formatTime(startMinutes, is24Hour)
    val end = formatTime(endMinutes, is24Hour)
    return when {
      start.isEmpty() && end.isEmpty() -> ""
      start.isEmpty() -> "-- - $end"
      end.isEmpty() -> "$start - --"
      else -> "$start - $end"
    }
  }

  private fun is24HourFormat(): Boolean {
    return _is24Hour.value ?: android.text.format.DateFormat.is24HourFormat(context)
  }

  @Composable
  override fun ComposeContent() {
    val disabled = _disabled.value
    val label = _label.value
    val placeholder = _placeholder.value
    val showModeToggle = _showModeToggle.value
    val is24Hour = is24HourFormat()

    // Get initial hour and minute from selected times
    val startHour = _startTimeMinutes.value?.let { it / 60 } ?: 0
    val startMinute = _startTimeMinutes.value?.let { it % 60 } ?: 0
    val endHour = _endTimeMinutes.value?.let { it / 60 } ?: 0
    val endMinute = _endTimeMinutes.value?.let { it % 60 } ?: 0

    // Create TimePicker states for start and end times
    val startTimePickerState = rememberTimePickerState(
      initialHour = startHour,
      initialMinute = startMinute,
      is24Hour = is24Hour
    )
    val endTimePickerState = rememberTimePickerState(
      initialHour = endHour,
      initialMinute = endMinute,
      is24Hour = is24Hour
    )

    // Mode toggle state: true = dial (TimePicker), false = input (TimeInput)
    var showDial by remember { mutableStateOf(_initialDisplayMode.value == "picker") }

    // Tab state: 0 = start time, 1 = end time
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Interaction source to detect clicks on the read-only text field
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
      interactionSource.interactions.collect { interaction ->
        if (interaction is PressInteraction.Release && !disabled) {
          _showDialog = true
          selectedTabIndex = 0 // Reset to start time tab when opening
        }
      }
    }

    // OutlinedTextField that shows selected time range
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = formatTimeRange(_startTimeMinutes.value, _endTimeMinutes.value, is24Hour),
      onValueChange = {},
      readOnly = true,
      singleLine = true,
      enabled = !disabled,
      label = label?.let { { Text(it) } },
      placeholder = placeholder?.let { { Text(it, maxLines = 1) } },
      trailingIcon = {
        Icon(Icons.Default.DateRange, contentDescription = "Select time range")
      },
      interactionSource = interactionSource
    )

    // TimeRangePickerDialog
    if (_showDialog) {
      AlertDialog(
        onDismissRequest = {
          _showDialog = false
          dispatchEvent(CancelEvent(getSurfaceId(), id))
        },
        confirmButton = {
          TextButton(
            onClick = {
              _showDialog = false
              val startMinutes = startTimePickerState.hour * 60 + startTimePickerState.minute
              val endMinutes = endTimePickerState.hour * 60 + endTimePickerState.minute
              dispatchEvent(TimeRangeConfirmEvent(getSurfaceId(), id, startMinutes, endMinutes))
            }
          ) {
            Text(_confirmLabel.value ?: "OK")
          }
        },
        dismissButton = {
          TextButton(
            onClick = {
              _showDialog = false
              dispatchEvent(CancelEvent(getSurfaceId(), id))
            }
          ) {
            Text(_cancelLabel.value ?: "Cancel")
          }
        },
        title = _titleText.value?.let { { Text(it) } },
        text = {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Tab row for switching between start and end time
            TabRow(selectedTabIndex = selectedTabIndex) {
              Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Start") }
              )
              Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("End") }
              )
            }

            // Mode toggle row
            if (showModeToggle) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.End
              ) {
                IconButton(onClick = { showDial = !showDial }) {
                  Icon(
                    imageVector = if (showDial) Icons.Default.Edit else Icons.Default.DateRange,
                    contentDescription = if (showDial) "Switch to keyboard input" else "Switch to dial"
                  )
                }
              }
            }

            // Time picker content based on selected tab
            val currentState = if (selectedTabIndex == 0) startTimePickerState else endTimePickerState
            if (showDial) {
              TimePicker(state = currentState)
            } else {
              TimeInput(state = currentState)
            }
          }
        }
      )

      // Track selection changes and dispatch to JS
      LaunchedEffect(
        startTimePickerState.hour,
        startTimePickerState.minute,
        endTimePickerState.hour,
        endTimePickerState.minute
      ) {
        val startMinutes = startTimePickerState.hour * 60 + startTimePickerState.minute
        val endMinutes = endTimePickerState.hour * 60 + endTimePickerState.minute
        dispatchEvent(TimeRangeChangeEvent(getSurfaceId(), id, startMinutes, endMinutes))
      }
    }
  }
}
