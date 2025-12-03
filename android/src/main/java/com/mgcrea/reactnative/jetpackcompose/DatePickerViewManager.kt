package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.DatePickerViewManagerDelegate
import com.facebook.react.viewmanagers.DatePickerViewManagerInterface

internal class DatePickerViewManager :
  SimpleViewManager<DatePickerView>(),
  DatePickerViewManagerInterface<DatePickerView> {

  private val delegate: ViewManagerDelegate<DatePickerView> =
    DatePickerViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): DatePickerView =
    DatePickerView(context)

  override fun getDelegate(): ViewManagerDelegate<DatePickerView> = delegate

  override fun setVisible(view: DatePickerView, value: Boolean) {
    view.setVisible(value)
  }

  override fun setIsInline(view: DatePickerView, value: Boolean) {
    view.setIsInline(value)
  }

  override fun setSelectedDateMillis(view: DatePickerView, value: Double) {
    view.setSelectedDateMillis(value)
  }

  override fun setInitialDisplayedMonthMillis(view: DatePickerView, value: Double) {
    view.setInitialDisplayedMonthMillis(value)
  }

  override fun setMinDateMillis(view: DatePickerView, value: Double) {
    view.setMinDateMillis(value)
  }

  override fun setMaxDateMillis(view: DatePickerView, value: Double) {
    view.setMaxDateMillis(value)
  }

  override fun setYearRangeStart(view: DatePickerView, value: Double) {
    view.setYearRangeStart(value)
  }

  override fun setYearRangeEnd(view: DatePickerView, value: Double) {
    view.setYearRangeEnd(value)
  }

  override fun setInitialDisplayMode(view: DatePickerView, value: String?) {
    view.setInitialDisplayMode(value)
  }

  override fun setShowModeToggle(view: DatePickerView, value: Boolean) {
    view.setShowModeToggle(value)
  }

  override fun setConfirmLabel(view: DatePickerView, value: String?) {
    view.setConfirmLabel(value)
  }

  override fun setCancelLabel(view: DatePickerView, value: String?) {
    view.setCancelLabel(value)
  }

  override fun setTitleText(view: DatePickerView, value: String?) {
    view.setTitleText(value)
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> {
    return MapBuilder.builder<String, Any>()
      .put("topConfirm", MapBuilder.of("registrationName", "onConfirm"))
      .put("topCancel", MapBuilder.of("registrationName", "onCancel"))
      .put("topDateChange", MapBuilder.of("registrationName", "onDateChange"))
      .build()
  }

  companion object {
    const val NAME = "DatePickerView"
  }
}
