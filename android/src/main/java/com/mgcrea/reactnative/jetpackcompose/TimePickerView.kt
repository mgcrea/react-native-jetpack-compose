package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
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
 * Read-only field that opens a Material3 time picker in an [AlertDialog].
 */
@OptIn(ExperimentalMaterial3Api::class)
internal class TimePickerView(reactContext: ThemedReactContext) : DialogPickerView(reactContext) {

  var selectedTimeMinutes: Int? by mutableStateOf(null)
  var is24Hour: Boolean? by mutableStateOf(null)

  @Composable
  override fun ComposeContent() {
    val use24Hour = is24Hour ?: rememberSystem24Hour()

    // Picker state lives only while the dialog is open, so it always starts from current props
    val state = if (showDialog) remember { timePickerState(selectedTimeMinutes, use24Hour) } else null

    // Report selections made in the dialog (skipping the initial value)
    LaunchedEffect(state) {
      if (state == null) return@LaunchedEffect
      snapshotFlow { state.minutesFromMidnight }
        .drop(1)
        .collect { minutes -> emitEvent(EventNames.TIME_CHANGE) { putInt("selectedTimeMinutes", minutes) } }
    }

    PickerTextField(
      value = DateTimeFormat.time(selectedTimeMinutes, use24Hour),
      label = label,
      placeholder = placeholder,
      enabled = !disabled,
      trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select time") },
      onPress = ::openDialog,
    )

    if (state != null) {
      var showDial by remember { mutableStateOf(!startsInInputMode) }
      AlertDialog(
        onDismissRequest = ::cancelDialog,
        confirmButton = {
          ConfirmButton {
            confirmDialog {
              emitEvent(EventNames.CONFIRM) { putInt("selectedTimeMinutes", state.minutesFromMidnight) }
            }
          }
        },
        dismissButton = { DismissButton() },
        title = dialogTitle(),
        text = {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TimePickerContent(
              state = state,
              showDial = showDial,
              showModeToggle = showModeToggle,
              onToggleMode = { showDial = !showDial },
            )
          }
        },
      )
    }
  }
}
