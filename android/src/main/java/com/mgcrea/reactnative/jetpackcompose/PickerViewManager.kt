package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.PickerViewManagerDelegate
import com.facebook.react.viewmanagers.PickerViewManagerInterface

@ReactModule(name = PickerViewManager.NAME)
internal class PickerViewManager :
  SimpleViewManager<PickerView>(),
  PickerViewManagerInterface<PickerView> {

  private val delegate: ViewManagerDelegate<PickerView> =
    PickerViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): PickerView =
    PickerView(context)

  override fun getDelegate(): ViewManagerDelegate<PickerView> = delegate

  override fun onDropViewInstance(view: PickerView) {
    super.onDropViewInstance(view)
    view.onDropInstance()
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: PickerView) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }

  override fun setItems(view: PickerView, value: ReadableArray?) {
    view.setItems(value)
  }

  override fun setSelectedValue(view: PickerView, value: String?) {
    view.setSelectedValue(value)
  }

  override fun setLabel(view: PickerView, value: String?) {
    view.setLabel(value)
  }

  override fun setPlaceholder(view: PickerView, value: String?) {
    view.setPlaceholder(value)
  }

  override fun setDisabled(view: PickerView, value: Boolean) {
    view.setDisabled(value)
  }

  companion object {
    const val NAME = "PickerView"
  }
}
