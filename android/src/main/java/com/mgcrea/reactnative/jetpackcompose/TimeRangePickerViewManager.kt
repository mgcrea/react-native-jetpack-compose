package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.TimeRangePickerViewManagerDelegate
import com.facebook.react.viewmanagers.TimeRangePickerViewManagerInterface

internal class TimeRangePickerViewManager :
  SimpleViewManager<TimeRangePickerView>(),
  TimeRangePickerViewManagerInterface<TimeRangePickerView> {

  private val delegate: ViewManagerDelegate<TimeRangePickerView> =
    TimeRangePickerViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): TimeRangePickerView =
    TimeRangePickerView(context)

  override fun getDelegate(): ViewManagerDelegate<TimeRangePickerView> = delegate

  override fun onDropViewInstance(view: TimeRangePickerView) {
    super.onDropViewInstance(view)
    view.onDropInstance()
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: TimeRangePickerView) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }

  override fun setStartTimeMinutes(view: TimeRangePickerView, value: Int) {
    view.setStartTimeMinutes(value)
  }

  override fun setEndTimeMinutes(view: TimeRangePickerView, value: Int) {
    view.setEndTimeMinutes(value)
  }

  override fun setIs24Hour(view: TimeRangePickerView, value: Boolean) {
    view.setIs24Hour(value)
  }

  override fun setInitialDisplayMode(view: TimeRangePickerView, value: String?) {
    view.setInitialDisplayMode(value)
  }

  override fun setShowModeToggle(view: TimeRangePickerView, value: Boolean) {
    view.setShowModeToggle(value)
  }

  override fun setConfirmLabel(view: TimeRangePickerView, value: String?) {
    view.setConfirmLabel(value)
  }

  override fun setCancelLabel(view: TimeRangePickerView, value: String?) {
    view.setCancelLabel(value)
  }

  override fun setTitleText(view: TimeRangePickerView, value: String?) {
    view.setTitleText(value)
  }

  override fun setLabel(view: TimeRangePickerView, value: String?) {
    view.setLabel(value)
  }

  override fun setPlaceholder(view: TimeRangePickerView, value: String?) {
    view.setPlaceholder(value)
  }

  override fun setDisabled(view: TimeRangePickerView, value: Boolean) {
    view.setDisabled(value)
  }

  companion object {
    const val NAME = "TimeRangePickerView"
  }
}
