package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.events.CancelEvent
import com.mgcrea.reactnative.jetpackcompose.events.DateRangeChangeEvent
import com.mgcrea.reactnative.jetpackcompose.events.RangeConfirmEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
internal class DateRangePickerView(reactContext: ThemedReactContext) :
  InlineComposeView(reactContext, TAG) {

  companion object {
    private const val TAG = "DateRangePickerView"
  }

  // State backing for Compose
  private val _startDateMillis = mutableStateOf<Long?>(null)
  private val _endDateMillis = mutableStateOf<Long?>(null)
  private val _initialDisplayedMonthMillis = mutableStateOf<Long?>(null)
  private val _minDateMillis = mutableStateOf<Long?>(null)
  private val _maxDateMillis = mutableStateOf<Long?>(null)
  private val _yearRangeStart = mutableStateOf<Int?>(null)
  private val _yearRangeEnd = mutableStateOf<Int?>(null)
  private val _initialDisplayMode = mutableStateOf("picker")
  private val _showModeToggle = mutableStateOf(true)
  private val _confirmLabel = mutableStateOf<String?>(null)
  private val _cancelLabel = mutableStateOf<String?>(null)
  private val _titleText = mutableStateOf<String?>(null)

  // New state for OutlinedTextField
  private val _label = mutableStateOf<String?>(null)
  private val _placeholder = mutableStateOf<String?>(null)
  private val _disabled = mutableStateOf(false)
  private var _showDialog by mutableStateOf(false)

  // Date formatting
  private val dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault())

  private fun formatDateRange(startMillis: Long?, endMillis: Long?): String {
    val startStr = startMillis?.let { dateFormat.format(Date(it)) }
    val endStr = endMillis?.let { dateFormat.format(Date(it)) }
    return when {
      startStr != null && endStr != null -> "$startStr - $endStr"
      startStr != null -> startStr
      else -> ""
    }
  }

  // Property setters called by ViewManager
  fun setStartDateMillis(value: Double?) {
    _startDateMillis.value = value?.toLong()
  }

  fun setEndDateMillis(value: Double?) {
    _endDateMillis.value = value?.toLong()
  }

  fun setInitialDisplayedMonthMillis(value: Double?) {
    _initialDisplayedMonthMillis.value = value?.toLong()
  }

  fun setMinDateMillis(value: Double?) {
    _minDateMillis.value = value?.toLong()
  }

  fun setMaxDateMillis(value: Double?) {
    _maxDateMillis.value = value?.toLong()
  }

  fun setYearRangeStart(value: Double?) {
    _yearRangeStart.value = value?.toInt()
  }

  fun setYearRangeEnd(value: Double?) {
    _yearRangeEnd.value = value?.toInt()
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

  @Composable
  override fun Content() {
    val disabled = _disabled.value
    val label = _label.value
    val placeholder = _placeholder.value
    val showModeToggle = _showModeToggle.value

    // Build year range
    val yearRange = remember(_yearRangeStart.value, _yearRangeEnd.value) {
      val start = _yearRangeStart.value ?: 1900
      val end = _yearRangeEnd.value ?: 2100
      start..end
    }

    // Build selectable dates for min/max constraints
    val selectableDates = remember(_minDateMillis.value, _maxDateMillis.value) {
      object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
          val min = _minDateMillis.value
          val max = _maxDateMillis.value
          return (min == null || utcTimeMillis >= min) &&
            (max == null || utcTimeMillis <= max)
        }

        override fun isSelectableYear(year: Int): Boolean {
          return year in yearRange
        }
      }
    }

    // Determine initial display mode
    val initialDisplayMode = remember(_initialDisplayMode.value) {
      when (_initialDisplayMode.value) {
        "input" -> DisplayMode.Input
        else -> DisplayMode.Picker
      }
    }

    // Create DateRangePicker state
    val dateRangePickerState = rememberDateRangePickerState(
      initialSelectedStartDateMillis = _startDateMillis.value,
      initialSelectedEndDateMillis = _endDateMillis.value,
      initialDisplayedMonthMillis = _initialDisplayedMonthMillis.value ?: _startDateMillis.value,
      yearRange = yearRange,
      initialDisplayMode = initialDisplayMode,
      selectableDates = selectableDates
    )

    // Track selection changes
    LaunchedEffect(
      dateRangePickerState.selectedStartDateMillis,
      dateRangePickerState.selectedEndDateMillis
    ) {
      dispatchEvent(DateRangeChangeEvent(
        getSurfaceId(),
        id,
        dateRangePickerState.selectedStartDateMillis,
        dateRangePickerState.selectedEndDateMillis
      ))
    }

    // Interaction source to detect clicks on the read-only text field
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
      interactionSource.interactions.collect { interaction ->
        if (interaction is PressInteraction.Release && !disabled) {
          _showDialog = true
        }
      }
    }

    // OutlinedTextField that shows selected date range
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = formatDateRange(_startDateMillis.value, _endDateMillis.value),
      onValueChange = {},
      readOnly = true,
      singleLine = true,
      enabled = !disabled,
      label = label?.let { { Text(it) } },
      placeholder = placeholder?.let { { Text(it, maxLines = 1) } },
      trailingIcon = {
        Icon(Icons.Default.DateRange, contentDescription = "Select date range")
      },
      interactionSource = interactionSource
    )

    // DatePickerDialog with DateRangePicker
    if (_showDialog) {
      DatePickerDialog(
        onDismissRequest = {
          _showDialog = false
          dispatchEvent(CancelEvent(getSurfaceId(), id))
        },
        confirmButton = {
          TextButton(
            onClick = {
              _showDialog = false
              val startMillis = dateRangePickerState.selectedStartDateMillis ?: 0L
              val endMillis = dateRangePickerState.selectedEndDateMillis ?: 0L
              dispatchEvent(RangeConfirmEvent(getSurfaceId(), id, startMillis, endMillis))
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
        }
      ) {
        DateRangePicker(
          state = dateRangePickerState,
          showModeToggle = showModeToggle,
          title = _titleText.value?.let { { Text(it) } },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}
