package com.mgcrea.reactnative.jetpackcompose

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import androidx.activity.ComponentDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.DialogHostView
import com.mgcrea.reactnative.jetpackcompose.events.CancelEvent
import com.mgcrea.reactnative.jetpackcompose.events.ConfirmEvent
import com.mgcrea.reactnative.jetpackcompose.events.DateChangeEvent

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewConstructor")
internal class DatePickerView(reactContext: ThemedReactContext) :
  DialogHostView(reactContext, TAG) {

  companion object {
    private const val TAG = "DatePickerView"
  }

  // State backing for Compose
  private val _isInline = mutableStateOf(false)
  private val _selectedDateMillis = mutableStateOf<Long?>(null)
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

  private var composeView: ComposeView? = null

  // Property setters called by ViewManager
  fun setIsInline(value: Boolean) {
    _isInline.value = value
  }

  fun setSelectedDateMillis(value: Double?) {
    _selectedDateMillis.value = value?.toLong()
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

  override fun createDialog(): Dialog {
    val activity = reactContext.currentActivity ?: throw IllegalStateException("No activity")

    composeView = ComposeView(activity).apply {
      setContent {
        DatePickerDialogContent()
      }
    }

    return ComponentDialog(activity).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      setContentView(composeView!!, ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      ))
      window?.apply {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
      }
      setOnDismissListener {
        if (isVisible) {
          markDismissed()
          dispatchEvent(CancelEvent(getSurfaceId(), id))
        }
      }
    }
  }

  @Composable
  private fun DatePickerDialogContent() {
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

    // Create DatePicker state
    val datePickerState = rememberDatePickerState(
      initialSelectedDateMillis = _selectedDateMillis.value,
      initialDisplayedMonthMillis = _initialDisplayedMonthMillis.value ?: _selectedDateMillis.value,
      yearRange = yearRange,
      initialDisplayMode = initialDisplayMode,
      selectableDates = selectableDates
    )

    // Track selection changes
    LaunchedEffect(datePickerState.selectedDateMillis) {
      datePickerState.selectedDateMillis?.let { millis ->
        dispatchEvent(DateChangeEvent(getSurfaceId(), id, millis))
      }
    }

    if (_isInline.value) {
      // Inline/embedded DatePicker (no dialog chrome)
      DatePicker(
        state = datePickerState,
        modifier = Modifier.fillMaxWidth(),
        showModeToggle = showModeToggle,
        title = _titleText.value?.let { { Text(it) } }
      )
    } else {
      // Modal DatePickerDialog with confirm/cancel buttons
      DatePickerDialog(
        onDismissRequest = {
          markDismissed()
          dispatchEvent(CancelEvent(getSurfaceId(), id))
          dialog?.dismiss()
        },
        confirmButton = {
          TextButton(
            onClick = {
              markDismissed()
              datePickerState.selectedDateMillis?.let { millis ->
                dispatchEvent(ConfirmEvent(getSurfaceId(), id, millis))
              }
              dialog?.dismiss()
            }
          ) {
            Text(_confirmLabel.value ?: "OK")
          }
        },
        dismissButton = {
          TextButton(
            onClick = {
              markDismissed()
              dispatchEvent(CancelEvent(getSurfaceId(), id))
              dialog?.dismiss()
            }
          ) {
            Text(_cancelLabel.value ?: "Cancel")
          }
        }
      ) {
        DatePicker(
          state = datePickerState,
          showModeToggle = showModeToggle,
          title = _titleText.value?.let { { Text(it) } }
        )
      }
    }
  }
}
