package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.DateTimeFormat
import com.mgcrea.reactnative.jetpackcompose.core.DialogPickerView
import com.mgcrea.reactnative.jetpackcompose.core.emitEvent
import com.mgcrea.reactnative.jetpackcompose.events.EventNames
import com.mgcrea.reactnative.jetpackcompose.ui.DEFAULT_YEAR_RANGE
import com.mgcrea.reactnative.jetpackcompose.ui.PickerTextField
import com.mgcrea.reactnative.jetpackcompose.ui.selectableDates
import java.util.Locale
import kotlinx.coroutines.flow.drop

/**
 * Read-only field that opens a Material3 [DateRangePicker] in a [DatePickerDialog].
 */
@OptIn(ExperimentalMaterial3Api::class)
internal class DateRangePickerView(reactContext: ThemedReactContext) : DialogPickerView(reactContext) {

  var startDateMillis: Long? by mutableStateOf(null)
  var endDateMillis: Long? by mutableStateOf(null)
  var initialDisplayedMonthMillis: Long? by mutableStateOf(null)
  var minDateMillis: Long? by mutableStateOf(null)
  var maxDateMillis: Long? by mutableStateOf(null)
  var yearRangeStart: Int? by mutableStateOf(null)
  var yearRangeEnd: Int? by mutableStateOf(null)

  @Composable
  override fun ComposeContent() {
    // Picker state lives only while the dialog is open, so it always starts from current props
    val state = if (showDialog) {
      remember {
        val years = (yearRangeStart ?: DEFAULT_YEAR_RANGE.first)..(yearRangeEnd ?: DEFAULT_YEAR_RANGE.last)
        DateRangePickerState(
          locale = Locale.getDefault(),
          initialSelectedStartDateMillis = startDateMillis,
          initialSelectedEndDateMillis = endDateMillis,
          initialDisplayedMonthMillis = initialDisplayedMonthMillis ?: startDateMillis,
          yearRange = years,
          initialDisplayMode = if (startsInInputMode) DisplayMode.Input else DisplayMode.Picker,
          selectableDates = selectableDates(minDateMillis, maxDateMillis, years),
        )
      }
    } else {
      null
    }

    // Report selections made in the dialog (skipping the initial value)
    LaunchedEffect(state) {
      if (state == null) return@LaunchedEffect
      snapshotFlow { state.selectedStartDateMillis to state.selectedEndDateMillis }
        .drop(1)
        .collect { (start, end) ->
          emitEvent(EventNames.DATE_CHANGE) {
            putDoubleOrNull("startDateMillis", start)
            putDoubleOrNull("endDateMillis", end)
          }
        }
    }

    // While the dialog is open, preview the in-progress selection
    val shownStart = if (state != null) state.selectedStartDateMillis else startDateMillis
    val shownEnd = if (state != null) state.selectedEndDateMillis else endDateMillis
    PickerTextField(
      value = DateTimeFormat.dateRange(shownStart, shownEnd),
      label = label,
      placeholder = placeholder,
      enabled = !disabled,
      trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select date range") },
      onPress = ::openDialog,
    )

    if (state != null) {
      DatePickerDialog(
        onDismissRequest = ::cancelDialog,
        confirmButton = {
          ConfirmButton {
            confirmDialog {
              val start = state.selectedStartDateMillis
              val end = state.selectedEndDateMillis
              // Only dispatch confirm event when both dates are selected
              if (start != null && end != null) {
                emitEvent(EventNames.CONFIRM) {
                  putDouble("startDateMillis", start.toDouble())
                  putDouble("endDateMillis", end.toDouble())
                }
              }
            }
          }
        },
        dismissButton = { DismissButton() },
      ) {
        DateRangePicker(
          state = state,
          showModeToggle = showModeToggle,
          title = dialogTitle(),
          modifier = Modifier.weight(1f),
        )
      }
    }
  }

  private fun WritableMap.putDoubleOrNull(key: String, value: Long?) {
    if (value != null) putDouble(key, value.toDouble()) else putNull(key)
  }
}
