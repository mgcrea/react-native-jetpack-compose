package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.TimePickerViewManagerDelegate
import com.facebook.react.viewmanagers.TimePickerViewManagerInterface

internal class TimePickerViewManager :
  SimpleViewManager<TimePickerView>(),
  TimePickerViewManagerInterface<TimePickerView> {

  private val delegate: ViewManagerDelegate<TimePickerView> =
    TimePickerViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): TimePickerView =
    TimePickerView(context)

  override fun getDelegate(): ViewManagerDelegate<TimePickerView> = delegate

  override fun onDropViewInstance(view: TimePickerView) {
    super.onDropViewInstance(view)
    view.onDropInstance()
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: TimePickerView) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }

  override fun setSelectedTimeMinutes(view: TimePickerView, value: Int) {
    view.setSelectedTimeMinutes(value)
  }

  override fun setIs24Hour(view: TimePickerView, value: Boolean) {
    view.setIs24Hour(value)
  }

  override fun setInitialDisplayMode(view: TimePickerView, value: String?) {
    view.setInitialDisplayMode(value)
  }

  override fun setShowModeToggle(view: TimePickerView, value: Boolean) {
    view.setShowModeToggle(value)
  }

  override fun setConfirmLabel(view: TimePickerView, value: String?) {
    view.setConfirmLabel(value)
  }

  override fun setCancelLabel(view: TimePickerView, value: String?) {
    view.setCancelLabel(value)
  }

  override fun setTitleText(view: TimePickerView, value: String?) {
    view.setTitleText(value)
  }

  override fun setLabel(view: TimePickerView, value: String?) {
    view.setLabel(value)
  }

  override fun setPlaceholder(view: TimePickerView, value: String?) {
    view.setPlaceholder(value)
  }

  override fun setDisabled(view: TimePickerView, value: Boolean) {
    view.setDisabled(value)
  }

  companion object {
    const val NAME = "TimePickerView"
  }
}
