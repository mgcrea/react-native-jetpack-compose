package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
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
 * Read-only field that opens a Material3 [DatePickerDialog].
 */
@OptIn(ExperimentalMaterial3Api::class)
internal class DatePickerView(reactContext: ThemedReactContext) : DialogPickerView(reactContext) {

  var selectedDateMillis: Long? by mutableStateOf(null)
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
        DatePickerState(
          locale = Locale.getDefault(),
          initialSelectedDateMillis = selectedDateMillis,
          initialDisplayedMonthMillis = initialDisplayedMonthMillis ?: selectedDateMillis,
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
      snapshotFlow { state.selectedDateMillis }
        .drop(1)
        .collect { millis ->
          if (millis != null) {
            emitEvent(EventNames.DATE_CHANGE) { putDouble("selectedDateMillis", millis.toDouble()) }
          }
        }
    }

    // While the dialog is open, preview the in-progress selection
    val shownMillis = if (state != null) state.selectedDateMillis else selectedDateMillis
    PickerTextField(
      value = DateTimeFormat.date(shownMillis),
      label = label,
      placeholder = placeholder,
      enabled = !disabled,
      trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select date") },
      onPress = ::openDialog,
    )

    if (state != null) {
      DatePickerDialog(
        onDismissRequest = ::cancelDialog,
        confirmButton = {
          ConfirmButton {
            confirmDialog {
              state.selectedDateMillis?.let { millis ->
                emitEvent(EventNames.CONFIRM) { putDouble("selectedDateMillis", millis.toDouble()) }
              }
            }
          }
        },
        dismissButton = { DismissButton() },
      ) {
        DatePicker(
          state = state,
          showModeToggle = showModeToggle,
          title = dialogTitle(),
        )
      }
    }
  }
}
