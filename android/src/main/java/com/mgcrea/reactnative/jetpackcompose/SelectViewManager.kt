package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.SelectViewManagerDelegate
import com.facebook.react.viewmanagers.SelectViewManagerInterface
import com.facebook.react.common.MapBuilder

@ReactModule(name = SelectViewManager.NAME)
internal class SelectViewManager :
  SimpleViewManager<SelectView>(),
  SelectViewManagerInterface<SelectView> {

  private val delegate: ViewManagerDelegate<SelectView> =
    SelectViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): SelectView =
    SelectView(context)

  override fun getDelegate(): ViewManagerDelegate<SelectView> = delegate

  override fun setItems(view: SelectView, value: String?) {
    view.setItems(value)
  }

  override fun setSelectedValue(view: SelectView, value: String?) {
    view.setSelectedValue(value)
  }

  override fun setLabel(view: SelectView, value: String?) {
    view.setLabel(value)
  }

  override fun setPlaceholder(view: SelectView, value: String?) {
    view.setPlaceholder(value)
  }

  override fun setDisabled(view: SelectView, value: Boolean) {
    view.setDisabled(value)
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> {
    return MapBuilder.builder<String, Any>()
      .put("topValueChange", MapBuilder.of("registrationName", "onValueChange"))
      .build()
  }

  companion object {
    const val NAME = "SelectView"
  }
}
