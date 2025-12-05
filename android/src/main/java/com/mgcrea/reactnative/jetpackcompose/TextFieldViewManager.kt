package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.TextFieldViewManagerDelegate
import com.facebook.react.viewmanagers.TextFieldViewManagerInterface

@ReactModule(name = TextFieldViewManager.NAME)
internal class TextFieldViewManager :
  SimpleViewManager<TextFieldView>(),
  TextFieldViewManagerInterface<TextFieldView> {

  private val delegate: ViewManagerDelegate<TextFieldView> =
    TextFieldViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): TextFieldView =
    TextFieldView(context)

  override fun getDelegate(): ViewManagerDelegate<TextFieldView> = delegate

  override fun onDropViewInstance(view: TextFieldView) {
    super.onDropViewInstance(view)
    view.onDropInstance()
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: TextFieldView) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }

  override fun setValue(view: TextFieldView, value: String?) {
    view.setValue(value)
  }

  override fun setLabel(view: TextFieldView, value: String?) {
    view.setLabel(value)
  }

  override fun setPlaceholder(view: TextFieldView, value: String?) {
    view.setPlaceholder(value)
  }

  override fun setDisabled(view: TextFieldView, value: Boolean) {
    view.setDisabled(value)
  }

  override fun setEditable(view: TextFieldView, value: Boolean) {
    view.setEditable(value)
  }

  override fun setMultiline(view: TextFieldView, value: Boolean) {
    view.setMultiline(value)
  }

  override fun setMaxLength(view: TextFieldView, value: Double) {
    view.setMaxLength(value)
  }

  override fun setSecureTextEntry(view: TextFieldView, value: Boolean) {
    view.setSecureTextEntry(value)
  }

  override fun setError(view: TextFieldView, value: Boolean) {
    view.setError(value)
  }

  override fun setHelperText(view: TextFieldView, value: String?) {
    view.setHelperText(value)
  }

  companion object {
    const val NAME = "TextFieldView"
  }
}
