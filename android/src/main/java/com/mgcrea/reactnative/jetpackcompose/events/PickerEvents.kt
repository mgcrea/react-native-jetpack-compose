package com.mgcrea.reactnative.jetpackcompose.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

/**
 * Fired when an item is selected in a picker.
 * Note: We use "topItemSelect" instead of "topSelect" to avoid conflict with
 * React Native's built-in "topSelect" bubbling event.
 */
class ItemSelectEvent(surfaceId: Int, viewId: Int, private val value: String) :
  Event<ItemSelectEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putString("value", value)
    }

  companion object {
    const val EVENT_NAME = "topItemSelect"
  }
}

/**
 * Fired when the picker value changes
 */
class ValueChangeEvent(surfaceId: Int, viewId: Int, private val value: String, private val index: Int) :
  Event<ValueChangeEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putString("value", value)
      putDouble("index", index.toDouble())
    }

  companion object {
    const val EVENT_NAME = "topValueChange"
  }
}

/**
 * Fired when a sheet/dialog is dismissed
 */
class DismissEvent(surfaceId: Int, viewId: Int) :
  Event<DismissEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap = Arguments.createMap()

  companion object {
    const val EVENT_NAME = "topDismiss"
  }
}
