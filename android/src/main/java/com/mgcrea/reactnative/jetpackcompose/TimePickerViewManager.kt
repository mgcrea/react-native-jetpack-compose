package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.TimePickerViewManagerDelegate
import com.facebook.react.viewmanagers.TimePickerViewManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewManager
import com.mgcrea.reactnative.jetpackcompose.core.orNullIfUnset

internal class TimePickerViewManager :
  BaseComposeViewManager<TimePickerView>(NAME, ::TimePickerView),
  TimePickerViewManagerInterface<TimePickerView> {

  override fun createDelegate(): ViewManagerDelegate<TimePickerView> = TimePickerViewManagerDelegate(this)

  override fun setSelectedTimeMinutes(view: TimePickerView, value: Int) {
    view.selectedTimeMinutes = value.orNullIfUnset()
  }

  override fun setIs24Hour(view: TimePickerView, value: Boolean) {
    view.is24Hour = value
  }

  override fun setInitialDisplayMode(view: TimePickerView, value: String?) {
    view.initialDisplayMode = value
  }

  override fun setShowModeToggle(view: TimePickerView, value: Boolean) {
    view.showModeToggle = value
  }

  override fun setConfirmLabel(view: TimePickerView, value: String?) {
    view.confirmLabel = value
  }

  override fun setCancelLabel(view: TimePickerView, value: String?) {
    view.cancelLabel = value
  }

  override fun setTitleText(view: TimePickerView, value: String?) {
    view.titleText = value
  }

  override fun setLabel(view: TimePickerView, value: String?) {
    view.label = value
  }

  override fun setPlaceholder(view: TimePickerView, value: String?) {
    view.placeholder = value
  }

  override fun setDisabled(view: TimePickerView, value: Boolean) {
    view.disabled = value
  }

  companion object {
    const val NAME = "TimePickerView"
  }
}
