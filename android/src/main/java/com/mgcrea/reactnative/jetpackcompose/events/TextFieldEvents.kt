package com.mgcrea.reactnative.jetpackcompose.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

/**
 * Fired when the text field value changes.
 */
class TextFieldChangeEvent(surfaceId: Int, viewId: Int, private val text: String) :
  Event<TextFieldChangeEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putString("text", text)
    }

  companion object {
    const val EVENT_NAME = "topTextFieldChange"
  }
}

/**
 * Fired when the text field gains focus.
 */
class TextFieldFocusEvent(surfaceId: Int, viewId: Int) :
  Event<TextFieldFocusEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap = Arguments.createMap()

  companion object {
    const val EVENT_NAME = "topTextFieldFocus"
  }
}

/**
 * Fired when the text field loses focus.
 */
class TextFieldBlurEvent(surfaceId: Int, viewId: Int) :
  Event<TextFieldBlurEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap = Arguments.createMap()

  companion object {
    const val EVENT_NAME = "topTextFieldBlur"
  }
}
