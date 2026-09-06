package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.PickerViewManagerDelegate
import com.facebook.react.viewmanagers.PickerViewManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewManager
import com.mgcrea.reactnative.jetpackcompose.core.toOptions

internal class PickerViewManager :
  BaseComposeViewManager<PickerView>(NAME, ::PickerView),
  PickerViewManagerInterface<PickerView> {

  override fun createDelegate(): ViewManagerDelegate<PickerView> = PickerViewManagerDelegate(this)

  override fun setItems(view: PickerView, value: ReadableArray?) {
    view.items = value.toOptions()
  }

  override fun setSelectedValue(view: PickerView, value: String?) {
    view.selectedValue = value
  }

  override fun setLabel(view: PickerView, value: String?) {
    view.label = value
  }

  override fun setPlaceholder(view: PickerView, value: String?) {
    view.placeholder = value
  }

  override fun setDisabled(view: PickerView, value: Boolean) {
    view.disabled = value
  }

  companion object {
    const val NAME = "PickerView"
  }
}
