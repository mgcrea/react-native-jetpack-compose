package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.TimeRangePickerViewManagerDelegate
import com.facebook.react.viewmanagers.TimeRangePickerViewManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewManager
import com.mgcrea.reactnative.jetpackcompose.core.orNullIfUnset

internal class TimeRangePickerViewManager :
  BaseComposeViewManager<TimeRangePickerView>(NAME, ::TimeRangePickerView),
  TimeRangePickerViewManagerInterface<TimeRangePickerView> {

  override fun createDelegate(): ViewManagerDelegate<TimeRangePickerView> = TimeRangePickerViewManagerDelegate(this)

  override fun setStartTimeMinutes(view: TimeRangePickerView, value: Int) {
    view.startTimeMinutes = value.orNullIfUnset()
  }

  override fun setEndTimeMinutes(view: TimeRangePickerView, value: Int) {
    view.endTimeMinutes = value.orNullIfUnset()
  }

  override fun setIs24Hour(view: TimeRangePickerView, value: Boolean) {
    view.is24Hour = value
  }

  override fun setInitialDisplayMode(view: TimeRangePickerView, value: String?) {
    view.initialDisplayMode = value
  }

  override fun setShowModeToggle(view: TimeRangePickerView, value: Boolean) {
    view.showModeToggle = value
  }

  override fun setConfirmLabel(view: TimeRangePickerView, value: String?) {
    view.confirmLabel = value
  }

  override fun setCancelLabel(view: TimeRangePickerView, value: String?) {
    view.cancelLabel = value
  }

  override fun setTitleText(view: TimeRangePickerView, value: String?) {
    view.titleText = value
  }

  override fun setLabel(view: TimeRangePickerView, value: String?) {
    view.label = value
  }

  override fun setPlaceholder(view: TimeRangePickerView, value: String?) {
    view.placeholder = value
  }

  override fun setDisabled(view: TimeRangePickerView, value: Boolean) {
    view.disabled = value
  }

  companion object {
    const val NAME = "TimeRangePickerView"
  }
}
