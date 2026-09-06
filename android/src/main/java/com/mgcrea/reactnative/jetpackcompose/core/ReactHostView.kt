package com.mgcrea.reactnative.jetpackcompose.core

import android.view.View
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.EventDispatcher
import com.mgcrea.reactnative.jetpackcompose.events.ComposeEvent

/**
 * Contract shared by every native view this package exposes to React Native.
 * Lets a single base ViewManager wire the event dispatcher and tear the view down.
 */
interface ReactHostView {
  /** Fabric event dispatcher, set by the ViewManager in `addEventEmitters`. */
  var eventDispatcher: EventDispatcher?

  /** Called from the ViewManager's `onDropViewInstance` before the view is destroyed. */
  fun onDropInstance()

  /** Dispatches a Fabric event. Implementations decide on threading (see [InlineComposeView]). */
  fun dispatchEvent(event: Event<*>)
}

/**
 * Builds and dispatches a [ComposeEvent] named [name] with a payload assembled by [build].
 */
internal fun <V> V.emitEvent(name: String, build: WritableMap.() -> Unit = {}) where V : View, V : ReactHostView {
  dispatchEvent(ComposeEvent(UIManagerHelper.getSurfaceId(this), id, name, Arguments.createMap().apply(build)))
}
