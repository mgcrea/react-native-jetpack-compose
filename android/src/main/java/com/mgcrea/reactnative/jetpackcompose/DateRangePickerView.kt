package com.mgcrea.reactnative.jetpackcompose

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import androidx.activity.ComponentDialog
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.DialogHostView

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewConstructor")
internal class DateRangePickerView(reactContext: ThemedReactContext) :
  DialogHostView(reactContext, TAG) {

  companion object {
    private const val TAG = "DateRangePickerView"
  }

  // State backing for Compose
  private val _isInline = mutableStateOf(false)
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

  private var composeView: ComposeView? = null

  // Property setters called by ViewManager
  fun setIsInline(value: Boolean) {
    _isInline.value = value
  }

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

  override fun createDialog(): Dialog {
    val activity = reactContext.currentActivity ?: throw IllegalStateException("No activity")

    composeView = ComposeView(activity).apply {
      setContent {
        DateRangePickerDialogContent()
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
          dispatchEvent("topCancel")
        }
      }
    }
  }

  @Composable
  private fun DateRangePickerDialogContent() {
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
      dispatchEvent("topDateChange", mapOf(
        "startDateMillis" to dateRangePickerState.selectedStartDateMillis,
        "endDateMillis" to dateRangePickerState.selectedEndDateMillis
      ))
    }

    if (_isInline.value) {
      // Inline/embedded DateRangePicker (no dialog chrome)
      DateRangePicker(
        state = dateRangePickerState,
        modifier = Modifier.fillMaxSize(),
        showModeToggle = showModeToggle,
        title = _titleText.value?.let { { Text(it, modifier = Modifier.padding(start = 16.dp, top = 16.dp)) } }
      )
    } else {
      // Modal DatePickerDialog with DateRangePicker
      DatePickerDialog(
        onDismissRequest = {
          markDismissed()
          dispatchEvent("topCancel")
          dialog?.dismiss()
        },
        confirmButton = {
          TextButton(
            onClick = {
              markDismissed()
              dispatchEvent("topConfirm", mapOf(
                "startDateMillis" to dateRangePickerState.selectedStartDateMillis,
                "endDateMillis" to dateRangePickerState.selectedEndDateMillis
              ))
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
              dispatchEvent("topCancel")
              dialog?.dismiss()
            }
          ) {
            Text(_cancelLabel.value ?: "Cancel")
          }
        }
      ) {
        DateRangePicker(
          state = dateRangePickerState,
          showModeToggle = showModeToggle,
          title = _titleText.value?.let { { Text(it, modifier = Modifier.padding(start = 16.dp, top = 16.dp)) } },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}
