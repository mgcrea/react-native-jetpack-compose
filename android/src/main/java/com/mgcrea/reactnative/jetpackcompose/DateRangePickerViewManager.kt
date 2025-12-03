package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.DateRangePickerViewManagerDelegate
import com.facebook.react.viewmanagers.DateRangePickerViewManagerInterface

internal class DateRangePickerViewManager :
  SimpleViewManager<DateRangePickerView>(),
  DateRangePickerViewManagerInterface<DateRangePickerView> {

  private val delegate: ViewManagerDelegate<DateRangePickerView> =
    DateRangePickerViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): DateRangePickerView =
    DateRangePickerView(context)

  override fun getDelegate(): ViewManagerDelegate<DateRangePickerView> = delegate

  override fun addEventEmitters(reactContext: ThemedReactContext, view: DateRangePickerView) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }

  override fun setVisible(view: DateRangePickerView, value: Boolean) {
    view.setVisible(value)
  }

  override fun setIsInline(view: DateRangePickerView, value: Boolean) {
    view.setIsInline(value)
  }

  override fun setStartDateMillis(view: DateRangePickerView, value: Double) {
    view.setStartDateMillis(value)
  }

  override fun setEndDateMillis(view: DateRangePickerView, value: Double) {
    view.setEndDateMillis(value)
  }

  override fun setInitialDisplayedMonthMillis(view: DateRangePickerView, value: Double) {
    view.setInitialDisplayedMonthMillis(value)
  }

  override fun setMinDateMillis(view: DateRangePickerView, value: Double) {
    view.setMinDateMillis(value)
  }

  override fun setMaxDateMillis(view: DateRangePickerView, value: Double) {
    view.setMaxDateMillis(value)
  }

  override fun setYearRangeStart(view: DateRangePickerView, value: Double) {
    view.setYearRangeStart(value)
  }

  override fun setYearRangeEnd(view: DateRangePickerView, value: Double) {
    view.setYearRangeEnd(value)
  }

  override fun setInitialDisplayMode(view: DateRangePickerView, value: String?) {
    view.setInitialDisplayMode(value)
  }

  override fun setShowModeToggle(view: DateRangePickerView, value: Boolean) {
    view.setShowModeToggle(value)
  }

  override fun setConfirmLabel(view: DateRangePickerView, value: String?) {
    view.setConfirmLabel(value)
  }

  override fun setCancelLabel(view: DateRangePickerView, value: String?) {
    view.setCancelLabel(value)
  }

  override fun setTitleText(view: DateRangePickerView, value: String?) {
    view.setTitleText(value)
  }

  companion object {
    const val NAME = "DateRangePickerView"
  }
}
