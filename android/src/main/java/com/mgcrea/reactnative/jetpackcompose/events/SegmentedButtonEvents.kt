package com.mgcrea.reactnative.jetpackcompose.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

/**
 * Fired when selection changes in single-select mode.
 * Event name: "topValueChange" (maps to onValueChange prop)
 */
class SegmentValueChangeEvent(
  surfaceId: Int,
  viewId: Int,
  private val value: String,
  private val index: Int
) : Event<SegmentValueChangeEvent>(surfaceId, viewId) {

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
 * Fired when selection changes in multi-select mode.
 * Event name: "topValuesChange" (maps to onValuesChange prop)
 * Values are passed as a JSON-encoded string array.
 */
class SegmentValuesChangeEvent(
  surfaceId: Int,
  viewId: Int,
  private val values: List<String>
) : Event<SegmentValuesChangeEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      // Encode values as JSON string array for codegen compatibility
      val jsonValues = values.joinToString(separator = ",", prefix = "[", postfix = "]") { "\"$it\"" }
      putString("values", jsonValues)
    }

  companion object {
    const val EVENT_NAME = "topValuesChange"
  }
}
