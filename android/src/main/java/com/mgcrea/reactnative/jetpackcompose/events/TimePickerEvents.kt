package com.mgcrea.reactnative.jetpackcompose.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

/**
 * Fired when a time picker confirms a selection
 */
class TimeConfirmEvent(surfaceId: Int, viewId: Int, private val selectedTimeMinutes: Int) :
  Event<TimeConfirmEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putInt("selectedTimeMinutes", selectedTimeMinutes)
    }

  companion object {
    const val EVENT_NAME = "topConfirm"
  }
}

/**
 * Fired when a time range picker confirms a selection
 */
class TimeRangeConfirmEvent(
  surfaceId: Int,
  viewId: Int,
  private val startTimeMinutes: Int,
  private val endTimeMinutes: Int
) : Event<TimeRangeConfirmEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putInt("startTimeMinutes", startTimeMinutes)
      putInt("endTimeMinutes", endTimeMinutes)
    }

  companion object {
    const val EVENT_NAME = "topConfirm"
  }
}

/**
 * Fired when the selected time changes (for inline pickers)
 */
class TimeChangeEvent(surfaceId: Int, viewId: Int, private val selectedTimeMinutes: Int) :
  Event<TimeChangeEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putInt("selectedTimeMinutes", selectedTimeMinutes)
    }

  companion object {
    const val EVENT_NAME = "topTimeChange"
  }
}

/**
 * Fired when the selected time range changes (for inline pickers)
 */
class TimeRangeChangeEvent(
  surfaceId: Int,
  viewId: Int,
  private val startTimeMinutes: Int?,
  private val endTimeMinutes: Int?
) : Event<TimeRangeChangeEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      if (startTimeMinutes != null) {
        putInt("startTimeMinutes", startTimeMinutes)
      } else {
        putNull("startTimeMinutes")
      }
      if (endTimeMinutes != null) {
        putInt("endTimeMinutes", endTimeMinutes)
      } else {
        putNull("endTimeMinutes")
      }
    }

  companion object {
    const val EVENT_NAME = "topTimeChange"
  }
}
