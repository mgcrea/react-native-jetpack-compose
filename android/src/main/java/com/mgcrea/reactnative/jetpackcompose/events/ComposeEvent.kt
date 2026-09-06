package com.mgcrea.reactnative.jetpackcompose.events

import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

/**
 * Generic Fabric event carrying a pre-built payload.
 *
 * Every event this package emits has the same shape (a `top*` name plus a flat map),
 * so a single class replaces one subclass per event. Coalescing keeps the
 * [Event] defaults.
 */
class ComposeEvent(
  surfaceId: Int,
  viewId: Int,
  private val name: String,
  private val data: WritableMap,
) : Event<ComposeEvent>(surfaceId, viewId) {

  override fun getEventName(): String = name

  override fun getEventData(): WritableMap = data
}
