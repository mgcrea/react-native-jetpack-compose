package com.mgcrea.reactnative.jetpackcompose

import android.util.Log
import android.view.View
import android.widget.FrameLayout
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
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

@OptIn(ExperimentalMaterial3Api::class)
internal class DateRangePickerView(private val reactContext: ThemedReactContext) :
  FrameLayout(reactContext) {

  companion object {
    private const val TAG = "DateRangePickerView"
  }

  // State backing
  private val _visible = mutableStateOf(false)
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

  private val composeView = ComposeView(reactContext)

  init {
    composeView.setContent {
      DateRangePickerContent()
    }
    super.addView(composeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
  }

  // Property setters called by ViewManager
  fun setVisible(value: Boolean) {
    _visible.value = value
  }

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

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    _visible.value = false
  }

  // Event dispatchers
  private fun dispatchConfirmEvent(startDateMillis: Long?, endDateMillis: Long?) {
    UiThreadUtil.runOnUiThread {
      if (!reactContext.hasActiveReactInstance()) {
        Log.w(TAG, "Cannot dispatch confirm event: React instance is not active")
        return@runOnUiThread
      }
      if (id == View.NO_ID) {
        Log.w(TAG, "Cannot dispatch confirm event: View has no ID")
        return@runOnUiThread
      }
      try {
        val event: WritableMap = Arguments.createMap()
        event.putDouble("startDateMillis", startDateMillis?.toDouble() ?: 0.0)
        event.putDouble("endDateMillis", endDateMillis?.toDouble() ?: 0.0)
        reactContext
          .getJSModule(RCTEventEmitter::class.java)
          ?.receiveEvent(id, "topConfirm", event)
      } catch (e: Exception) {
        Log.w(TAG, "Failed to dispatch confirm event", e)
      }
    }
  }

  private fun dispatchCancelEvent() {
    UiThreadUtil.runOnUiThread {
      if (!reactContext.hasActiveReactInstance()) {
        Log.w(TAG, "Cannot dispatch cancel event: React instance is not active")
        return@runOnUiThread
      }
      if (id == View.NO_ID) {
        Log.w(TAG, "Cannot dispatch cancel event: View has no ID")
        return@runOnUiThread
      }
      try {
        val event: WritableMap = Arguments.createMap()
        reactContext
          .getJSModule(RCTEventEmitter::class.java)
          ?.receiveEvent(id, "topCancel", event)
      } catch (e: Exception) {
        Log.w(TAG, "Failed to dispatch cancel event", e)
      }
    }
  }

  private fun dispatchChangeEvent(startDateMillis: Long?, endDateMillis: Long?) {
    UiThreadUtil.runOnUiThread {
      if (!reactContext.hasActiveReactInstance()) {
        Log.w(TAG, "Cannot dispatch change event: React instance is not active")
        return@runOnUiThread
      }
      if (id == View.NO_ID) {
        Log.w(TAG, "Cannot dispatch change event: View has no ID")
        return@runOnUiThread
      }
      try {
        val event: WritableMap = Arguments.createMap()
        event.putDouble("startDateMillis", startDateMillis?.toDouble() ?: 0.0)
        event.putDouble("endDateMillis", endDateMillis?.toDouble() ?: 0.0)
        reactContext
          .getJSModule(RCTEventEmitter::class.java)
          ?.receiveEvent(id, "topDateChange", event)
      } catch (e: Exception) {
        Log.w(TAG, "Failed to dispatch change event", e)
      }
    }
  }

  @Composable
  private fun DateRangePickerContent() {
    val isVisible = _visible.value
    val isInline = _isInline.value
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
      dispatchChangeEvent(
        dateRangePickerState.selectedStartDateMillis,
        dateRangePickerState.selectedEndDateMillis
      )
    }

    if (isInline) {
      // Inline/embedded DateRangePicker
      if (isVisible) {
        DateRangePicker(
          state = dateRangePickerState,
          modifier = Modifier.fillMaxSize(),
          showModeToggle = showModeToggle,
          title = _titleText.value?.let { { Text(it, modifier = Modifier.padding(start = 16.dp, top = 16.dp)) } }
        )
      }
    } else {
      // Modal DatePickerDialog with DateRangePicker
      if (isVisible) {
        DatePickerDialog(
          onDismissRequest = {
            _visible.value = false
            dispatchCancelEvent()
          },
          confirmButton = {
            TextButton(
              onClick = {
                _visible.value = false
                dispatchConfirmEvent(
                  dateRangePickerState.selectedStartDateMillis,
                  dateRangePickerState.selectedEndDateMillis
                )
              }
            ) {
              Text(_confirmLabel.value ?: "OK")
            }
          },
          dismissButton = {
            TextButton(
              onClick = {
                _visible.value = false
                dispatchCancelEvent()
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
}
