package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.TextFieldViewManagerDelegate
import com.facebook.react.viewmanagers.TextFieldViewManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewManager

internal class TextFieldViewManager :
  BaseComposeViewManager<TextFieldView>(NAME, ::TextFieldView),
  TextFieldViewManagerInterface<TextFieldView> {

  override fun createDelegate(): ViewManagerDelegate<TextFieldView> = TextFieldViewManagerDelegate(this)

  override fun setValue(view: TextFieldView, value: String?) {
    view.value = value ?: ""
  }

  override fun setLabel(view: TextFieldView, value: String?) {
    view.label = value
  }

  override fun setPlaceholder(view: TextFieldView, value: String?) {
    view.placeholder = value
  }

  override fun setDisabled(view: TextFieldView, value: Boolean) {
    view.disabled = value
  }

  override fun setEditable(view: TextFieldView, value: Boolean) {
    view.editable = value
  }

  override fun setMultiline(view: TextFieldView, value: Boolean) {
    view.multiline = value
  }

  override fun setMaxLength(view: TextFieldView, value: Double) {
    view.maxLength = value.toInt().takeIf { it > 0 }
  }

  override fun setSecureTextEntry(view: TextFieldView, value: Boolean) {
    view.secureTextEntry = value
  }

  override fun setError(view: TextFieldView, value: Boolean) {
    view.error = value
  }

  override fun setHelperText(view: TextFieldView, value: String?) {
    view.helperText = value
  }

  override fun setKeyboardType(view: TextFieldView, value: String?) {
    view.keyboardType = value ?: "default"
  }

  override fun setReturnKeyType(view: TextFieldView, value: String?) {
    view.returnKeyType = value ?: "done"
  }

  override fun setAutoCapitalize(view: TextFieldView, value: String?) {
    view.autoCapitalize = value ?: "sentences"
  }

  override fun setAutoCorrect(view: TextFieldView, value: Boolean) {
    view.autoCorrect = value
  }

  override fun setLeadingIcon(view: TextFieldView, value: String?) {
    view.leadingIcon = value
  }

  override fun setTrailingIcon(view: TextFieldView, value: String?) {
    view.trailingIcon = value
  }

  override fun setShowCounter(view: TextFieldView, value: Boolean) {
    view.showCounter = value
  }

  companion object {
    const val NAME = "TextFieldView"
  }
}
