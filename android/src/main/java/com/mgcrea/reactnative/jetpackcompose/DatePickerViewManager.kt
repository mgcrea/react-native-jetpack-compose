package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.DatePickerViewManagerDelegate
import com.facebook.react.viewmanagers.DatePickerViewManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewManager
import com.mgcrea.reactnative.jetpackcompose.core.orNullIfUnset

internal class DatePickerViewManager :
  BaseComposeViewManager<DatePickerView>(NAME, ::DatePickerView),
  DatePickerViewManagerInterface<DatePickerView> {

  override fun createDelegate(): ViewManagerDelegate<DatePickerView> = DatePickerViewManagerDelegate(this)

  override fun setSelectedDateMillis(view: DatePickerView, value: Double) {
    view.selectedDateMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setInitialDisplayedMonthMillis(view: DatePickerView, value: Double) {
    view.initialDisplayedMonthMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setMinDateMillis(view: DatePickerView, value: Double) {
    view.minDateMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setMaxDateMillis(view: DatePickerView, value: Double) {
    view.maxDateMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setYearRangeStart(view: DatePickerView, value: Double) {
    view.yearRangeStart = value.orNullIfUnset()?.toInt()
  }

  override fun setYearRangeEnd(view: DatePickerView, value: Double) {
    view.yearRangeEnd = value.orNullIfUnset()?.toInt()
  }

  override fun setInitialDisplayMode(view: DatePickerView, value: String?) {
    view.initialDisplayMode = value
  }

  override fun setShowModeToggle(view: DatePickerView, value: Boolean) {
    view.showModeToggle = value
  }

  override fun setConfirmLabel(view: DatePickerView, value: String?) {
    view.confirmLabel = value
  }

  override fun setCancelLabel(view: DatePickerView, value: String?) {
    view.cancelLabel = value
  }

  override fun setTitleText(view: DatePickerView, value: String?) {
    view.titleText = value
  }

  override fun setLabel(view: DatePickerView, value: String?) {
    view.label = value
  }

  override fun setPlaceholder(view: DatePickerView, value: String?) {
    view.placeholder = value
  }

  override fun setDisabled(view: DatePickerView, value: Boolean) {
    view.disabled = value
  }

  companion object {
    const val NAME = "DatePickerView"
  }
}
