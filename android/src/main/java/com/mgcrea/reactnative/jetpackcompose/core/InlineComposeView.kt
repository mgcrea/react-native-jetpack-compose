package com.mgcrea.reactnative.jetpackcompose.core

import android.os.Handler
import android.os.Looper
import android.view.View.MeasureSpec
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.EventDispatcher

/**
 * Base class for inline Compose views in React Native.
 *
 * Uses AbstractComposeView which handles lifecycle setup internally,
 * eliminating the need for manual LifecycleOwner/SavedStateRegistryOwner setup.
 *
 * Subclasses implement [Content] to provide the Composable content.
 */
abstract class InlineComposeView(
  protected val reactContext: ThemedReactContext,
  private val tag: String
) : AbstractComposeView(reactContext) {

  /** Handler for posting events outside the Compose recomposition cycle */
  private val mainHandler = Handler(Looper.getMainLooper())

  /** Event dispatcher for Fabric events, set by ViewManager */
  var eventDispatcher: EventDispatcher? = null

  /** Flag to track if view has been detached - used to prevent use-after-free crashes */
  @Volatile
  protected var isDetached: Boolean = false
    private set

  /**
   * Called by ViewManager's onDropViewInstance before the view is destroyed.
   * Performs early cleanup to prevent use-after-free crashes in C++ props destructors.
   */
  open fun onDropInstance() {
    isDetached = true
    eventDispatcher = null
    mainHandler.removeCallbacksAndMessages(null)
  }

  override fun onDetachedFromWindow() {
    if (!isDetached) {
      isDetached = true
      eventDispatcher = null
      mainHandler.removeCallbacksAndMessages(null)
    }
    super.onDetachedFromWindow()
  }

  /**
   * Dispatches an event to JavaScript using the Fabric event system.
   * Posts the dispatch to the main handler to ensure it happens OUTSIDE
   * the Compose recomposition cycle, avoiding heap corruption in Fabric
   * Props destructors.
   *
   * Safely ignores dispatch if the view has been detached to prevent use-after-free crashes.
   *
   * @param event The event to dispatch
   */
  protected fun dispatchEvent(event: Event<*>) {
    if (isDetached) return
    val dispatcher = eventDispatcher ?: return
    mainHandler.post {
      if (!isDetached) {
        dispatcher.dispatchEvent(event)
      }
    }
  }

  /**
   * Gets the surface ID for event dispatching.
   */
  protected fun getSurfaceId(): Int = UIManagerHelper.getSurfaceId(this)

  /**
   * Required for React Native's layout system to work properly with Compose.
   * Posts a measure/layout pass after Compose requests layout.
   */
  override fun requestLayout() {
    super.requestLayout()
    post(measureAndLayout)
  }

  private val measureAndLayout = Runnable {
    measure(
      MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
    )
    layout(left, top, right, bottom)
  }
}
