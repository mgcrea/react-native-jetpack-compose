package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.DateRangePickerViewManagerDelegate
import com.facebook.react.viewmanagers.DateRangePickerViewManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewManager
import com.mgcrea.reactnative.jetpackcompose.core.orNullIfUnset

internal class DateRangePickerViewManager :
  BaseComposeViewManager<DateRangePickerView>(NAME, ::DateRangePickerView),
  DateRangePickerViewManagerInterface<DateRangePickerView> {

  override fun createDelegate(): ViewManagerDelegate<DateRangePickerView> = DateRangePickerViewManagerDelegate(this)

  override fun setStartDateMillis(view: DateRangePickerView, value: Double) {
    view.startDateMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setEndDateMillis(view: DateRangePickerView, value: Double) {
    view.endDateMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setInitialDisplayedMonthMillis(view: DateRangePickerView, value: Double) {
    view.initialDisplayedMonthMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setMinDateMillis(view: DateRangePickerView, value: Double) {
    view.minDateMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setMaxDateMillis(view: DateRangePickerView, value: Double) {
    view.maxDateMillis = value.orNullIfUnset()?.toLong()
  }

  override fun setYearRangeStart(view: DateRangePickerView, value: Double) {
    view.yearRangeStart = value.orNullIfUnset()?.toInt()
  }

  override fun setYearRangeEnd(view: DateRangePickerView, value: Double) {
    view.yearRangeEnd = value.orNullIfUnset()?.toInt()
  }

  override fun setInitialDisplayMode(view: DateRangePickerView, value: String?) {
    view.initialDisplayMode = value
  }

  override fun setShowModeToggle(view: DateRangePickerView, value: Boolean) {
    view.showModeToggle = value
  }

  override fun setConfirmLabel(view: DateRangePickerView, value: String?) {
    view.confirmLabel = value
  }

  override fun setCancelLabel(view: DateRangePickerView, value: String?) {
    view.cancelLabel = value
  }

  override fun setTitleText(view: DateRangePickerView, value: String?) {
    view.titleText = value
  }

  override fun setLabel(view: DateRangePickerView, value: String?) {
    view.label = value
  }

  override fun setPlaceholder(view: DateRangePickerView, value: String?) {
    view.placeholder = value
  }

  override fun setDisabled(view: DateRangePickerView, value: Boolean) {
    view.disabled = value
  }

  companion object {
    const val NAME = "DateRangePickerView"
  }
}
