package com.mgcrea.reactnative.jetpackcompose.core

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.events.EventNames

/**
 * Base class for the "read-only field that opens a confirm/cancel dialog" pickers
 * (date, date range, time, time range).
 *
 * Owns the props those pickers share and the dialog open/close bookkeeping.
 * Subclasses read [showDialog] and build their state per open so prop changes made
 * while the dialog is closed are always reflected the next time it opens.
 */
abstract class DialogPickerView(reactContext: ThemedReactContext) : InlineComposeView(reactContext) {

  var label: String? by mutableStateOf(null)
  var placeholder: String? by mutableStateOf(null)
  var disabled: Boolean by mutableStateOf(false)
  var confirmLabel: String? by mutableStateOf(null)
  var cancelLabel: String? by mutableStateOf(null)
  var titleText: String? by mutableStateOf(null)
  var initialDisplayMode: String? by mutableStateOf(null)
  var showModeToggle: Boolean by mutableStateOf(true)

  protected var showDialog: Boolean by mutableStateOf(false)

  /** True when the JS side asked for the keyboard/text input mode first. */
  protected val startsInInputMode: Boolean get() = initialDisplayMode == "input"

  override fun onDropInstance() {
    showDialog = false
    super.onDropInstance()
  }

  /** Opens the dialog unless the field is disabled. */
  protected fun openDialog() {
    if (!disabled) showDialog = true
  }

  /** Closes the dialog and emits the cancel event. */
  protected fun cancelDialog() {
    showDialog = false
    emitEvent(EventNames.CANCEL)
  }

  /** Closes the dialog and lets the caller emit the confirm event. */
  protected fun confirmDialog(emit: () -> Unit) {
    showDialog = false
    emit()
  }

  @Composable
  protected fun ConfirmButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) { Text(confirmLabel ?: "OK") }
  }

  @Composable
  protected fun DismissButton() {
    TextButton(onClick = ::cancelDialog) { Text(cancelLabel ?: "Cancel") }
  }

  /** Dialog title slot, or null when no title was set. */
  protected fun dialogTitle(): (@Composable () -> Unit)? = titleText?.let { { Text(it) } }
}
