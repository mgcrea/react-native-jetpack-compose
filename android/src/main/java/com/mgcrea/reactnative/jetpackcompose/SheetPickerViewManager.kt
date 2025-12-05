package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.SheetPickerViewManagerDelegate
import com.facebook.react.viewmanagers.SheetPickerViewManagerInterface

@ReactModule(name = SheetPickerViewManager.NAME)
internal class SheetPickerViewManager :
  SimpleViewManager<SheetPickerView>(),
  SheetPickerViewManagerInterface<SheetPickerView> {

  private val delegate: ViewManagerDelegate<SheetPickerView> =
    SheetPickerViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): SheetPickerView =
    SheetPickerView(context)

  override fun getDelegate(): ViewManagerDelegate<SheetPickerView> = delegate

  override fun onDropViewInstance(view: SheetPickerView) {
    super.onDropViewInstance(view)
    view.onDropInstance()
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: SheetPickerView) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }

  override fun setOptions(view: SheetPickerView, value: ReadableArray?) {
    view.setOptions(value)
  }

  override fun setSelectedValue(view: SheetPickerView, value: String?) {
    view.setSelectedValue(value)
  }

  override fun setTitle(view: SheetPickerView, value: String?) {
    view.setTitle(value)
  }

  override fun setSearchPlaceholder(view: SheetPickerView, value: String?) {
    view.setSearchPlaceholder(value)
  }

  override fun setAutoDismiss(view: SheetPickerView, value: Boolean) {
    view.setAutoDismiss(value)
  }

  override fun setMaxHeightRatio(view: SheetPickerView, value: Double) {
    view.setMaxHeightRatio(value)
  }

  override fun setSheetMaxWidth(view: SheetPickerView, value: Double) {
    view.setSheetMaxWidth(value)
  }

  override fun setLabel(view: SheetPickerView, value: String?) {
    view.setLabel(value)
  }

  override fun setPlaceholder(view: SheetPickerView, value: String?) {
    view.setPlaceholder(value)
  }

  override fun setDisabled(view: SheetPickerView, value: Boolean) {
    view.setDisabled(value)
  }

  companion object {
    const val NAME = "SheetPickerView"
  }
}
