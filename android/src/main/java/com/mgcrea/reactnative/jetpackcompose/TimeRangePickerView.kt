package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.DateTimeFormat
import com.mgcrea.reactnative.jetpackcompose.core.DialogPickerView
import com.mgcrea.reactnative.jetpackcompose.core.emitEvent
import com.mgcrea.reactnative.jetpackcompose.events.EventNames
import com.mgcrea.reactnative.jetpackcompose.ui.PickerTextField
import com.mgcrea.reactnative.jetpackcompose.ui.TimePickerContent
import com.mgcrea.reactnative.jetpackcompose.ui.minutesFromMidnight
import com.mgcrea.reactnative.jetpackcompose.ui.rememberSystem24Hour
import com.mgcrea.reactnative.jetpackcompose.ui.timePickerState
import kotlinx.coroutines.flow.drop

/**
 * Read-only field that opens a start/end time picker (tabbed) in an [AlertDialog].
 */
@OptIn(ExperimentalMaterial3Api::class)
internal class TimeRangePickerView(reactContext: ThemedReactContext) : DialogPickerView(reactContext) {

  var startTimeMinutes: Int? by mutableStateOf(null)
  var endTimeMinutes: Int? by mutableStateOf(null)
  var is24Hour: Boolean? by mutableStateOf(null)

  @Composable
  override fun ComposeContent() {
    val use24Hour = is24Hour ?: rememberSystem24Hour()

    // Picker states live only while the dialog is open, so they always start from current props
    val states = if (showDialog) {
      remember { timePickerState(startTimeMinutes, use24Hour) to timePickerState(endTimeMinutes, use24Hour) }
    } else {
      null
    }

    // Report selections made in the dialog (skipping the initial value)
    LaunchedEffect(states) {
      if (states == null) return@LaunchedEffect
      val (start, end) = states
      snapshotFlow { start.minutesFromMidnight to end.minutesFromMidnight }
        .drop(1)
        .collect { (startMinutes, endMinutes) ->
          emitEvent(EventNames.TIME_CHANGE) {
            putInt("startTimeMinutes", startMinutes)
            putInt("endTimeMinutes", endMinutes)
          }
        }
    }

    PickerTextField(
      value = DateTimeFormat.timeRange(startTimeMinutes, endTimeMinutes, use24Hour),
      label = label,
      placeholder = placeholder,
      enabled = !disabled,
      trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select time range") },
      onPress = ::openDialog,
    )

    if (states != null) {
      val (startState, endState) = states
      var showDial by remember { mutableStateOf(!startsInInputMode) }
      // 0 = start time, 1 = end time; resets to start on every open
      var selectedTab by remember { mutableIntStateOf(0) }

      AlertDialog(
        onDismissRequest = ::cancelDialog,
        confirmButton = {
          ConfirmButton {
            confirmDialog {
              emitEvent(EventNames.CONFIRM) {
                putInt("startTimeMinutes", startState.minutesFromMidnight)
                putInt("endTimeMinutes", endState.minutesFromMidnight)
              }
            }
          }
        },
        dismissButton = { DismissButton() },
        title = dialogTitle(),
        text = {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
              Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Start") })
              Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("End") })
            }
            TimePickerContent(
              state = if (selectedTab == 0) startState else endState,
              showDial = showDial,
              showModeToggle = showModeToggle,
              onToggleMode = { showDial = !showDial },
              toggleRowPadding = PaddingValues(vertical = 8.dp),
            )
          }
        },
      )
    }
  }
}
