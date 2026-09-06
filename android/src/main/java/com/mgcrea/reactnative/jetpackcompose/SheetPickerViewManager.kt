package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.SheetPickerViewManagerDelegate
import com.facebook.react.viewmanagers.SheetPickerViewManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewManager
import com.mgcrea.reactnative.jetpackcompose.core.toOptions

internal class SheetPickerViewManager :
  BaseComposeViewManager<SheetPickerView>(NAME, ::SheetPickerView),
  SheetPickerViewManagerInterface<SheetPickerView> {

  override fun createDelegate(): ViewManagerDelegate<SheetPickerView> = SheetPickerViewManagerDelegate(this)

  override fun setOptions(view: SheetPickerView, value: ReadableArray?) {
    view.options = value.toOptions()
  }

  override fun setSelectedValue(view: SheetPickerView, value: String?) {
    view.selectedValue = value
  }

  override fun setTitle(view: SheetPickerView, value: String?) {
    view.title = value
  }

  override fun setSearchPlaceholder(view: SheetPickerView, value: String?) {
    view.searchPlaceholder = value ?: "Search"
  }

  override fun setAutoDismiss(view: SheetPickerView, value: Boolean) {
    view.autoDismiss = value
  }

  override fun setMaxHeightRatio(view: SheetPickerView, value: Double) {
    view.maxHeightRatio = value.toFloat()
  }

  override fun setSheetMaxWidth(view: SheetPickerView, value: Double) {
    view.sheetMaxWidth = value.toFloat()
  }

  override fun setLabel(view: SheetPickerView, value: String?) {
    view.label = value
  }

  override fun setPlaceholder(view: SheetPickerView, value: String?) {
    view.placeholder = value
  }

  override fun setDisabled(view: SheetPickerView, value: Boolean) {
    view.disabled = value
  }

  companion object {
    const val NAME = "SheetPickerView"
  }
}
