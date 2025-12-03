package com.mgcrea.reactnative.jetpackcompose.events

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

/**
 * Fired when a date picker confirms a selection
 */
class ConfirmEvent(surfaceId: Int, viewId: Int, private val selectedDateMillis: Long) :
  Event<ConfirmEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putDouble("selectedDateMillis", selectedDateMillis.toDouble())
    }

  companion object {
    const val EVENT_NAME = "topConfirm"
  }
}

/**
 * Fired when a date range picker confirms a selection
 */
class RangeConfirmEvent(
  surfaceId: Int,
  viewId: Int,
  private val startDateMillis: Long,
  private val endDateMillis: Long
) : Event<RangeConfirmEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putDouble("startDateMillis", startDateMillis.toDouble())
      putDouble("endDateMillis", endDateMillis.toDouble())
    }

  companion object {
    const val EVENT_NAME = "topConfirm"
  }
}

/**
 * Fired when a date picker is cancelled
 */
class CancelEvent(surfaceId: Int, viewId: Int) :
  Event<CancelEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap = Arguments.createMap()

  companion object {
    const val EVENT_NAME = "topCancel"
  }
}

/**
 * Fired when the selected date changes (for inline pickers)
 */
class DateChangeEvent(surfaceId: Int, viewId: Int, private val selectedDateMillis: Long) :
  Event<DateChangeEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      putDouble("selectedDateMillis", selectedDateMillis.toDouble())
    }

  companion object {
    const val EVENT_NAME = "topDateChange"
  }
}

/**
 * Fired when the selected date range changes (for inline pickers)
 */
class DateRangeChangeEvent(
  surfaceId: Int,
  viewId: Int,
  private val startDateMillis: Long?,
  private val endDateMillis: Long?
) : Event<DateRangeChangeEvent>(surfaceId, viewId) {

  override fun getEventName(): String = EVENT_NAME

  override fun getEventData(): WritableMap =
    Arguments.createMap().apply {
      if (startDateMillis != null) {
        putDouble("startDateMillis", startDateMillis.toDouble())
      } else {
        putNull("startDateMillis")
      }
      if (endDateMillis != null) {
        putDouble("endDateMillis", endDateMillis.toDouble())
      } else {
        putNull("endDateMillis")
      }
    }

  companion object {
    const val EVENT_NAME = "topDateChange"
  }
}
