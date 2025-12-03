package com.mgcrea.reactnative.jetpackcompose.core

import android.util.Log
import android.view.View
import android.view.View.MeasureSpec
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.compositionContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Base class for inline Compose views in React Native.
 *
 * Handles lifecycle setup for ComposeView to work properly within
 * React Native's view hierarchy. Creates its own LifecycleOwner since
 * ReactActivity is not a ComponentActivity.
 *
 * Subclasses implement [ComposeContent] to provide the Composable content.
 */
abstract class InlineComposeView(
  protected val reactContext: ThemedReactContext,
  private val tag: String
) : FrameLayout(reactContext), LifecycleOwner, SavedStateRegistryOwner {

  private var composeView: ComposeView? = null

  // Create our own lifecycle since ReactActivity doesn't provide one
  private val lifecycleRegistry = LifecycleRegistry(this)
  private val savedStateRegistryController = SavedStateRegistryController.create(this)

  override val lifecycle: Lifecycle get() = lifecycleRegistry
  override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

  private var recomposerScope: CoroutineScope? = null

  /**
   * The Composable content to render.
   * Implement this in subclasses.
   */
  @Composable
  protected abstract fun ComposeContent()

  init {
    savedStateRegistryController.performRestore(null)
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    if (composeView == null) {
      setupComposeView()
    }
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    // Ensure ComposeView fills the available space from React Native layout
    composeView?.let { view ->
      val width = right - left
      val height = bottom - top
      view.measure(
        MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST)
      )
      view.layout(0, 0, width, view.measuredHeight)
    }
  }

  private fun setupComposeView() {
    // Set lifecycle owners on this view so ComposeView can find them
    setViewTreeLifecycleOwner(this)
    setViewTreeSavedStateRegistryOwner(this)

    // Create a coroutine scope for the recomposer
    val scope = CoroutineScope(AndroidUiDispatcher.CurrentThread)
    recomposerScope = scope

    // Create a Recomposer
    val recomposer = Recomposer(scope.coroutineContext)
    scope.launch {
      recomposer.runRecomposeAndApplyChanges()
    }

    composeView = ComposeView(context).apply {
      // Set composition context to our recomposer
      compositionContext = recomposer
      setContent { ComposeContent() }
    }
    addView(composeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
  }

  override fun onDetachedFromWindow() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    recomposerScope?.cancel()
    recomposerScope = null
    composeView?.let {
      removeView(it)
      composeView = null
    }
    super.onDetachedFromWindow()
  }

  /**
   * Dispatches an event to JavaScript via RCTEventEmitter.
   *
   * @param eventName The native event name (e.g., "topValueChange")
   * @param params Optional map of event parameters
   */
  protected fun dispatchEvent(eventName: String, params: Map<String, Any?> = emptyMap()) {
    UiThreadUtil.runOnUiThread {
      if (!reactContext.hasActiveReactInstance()) {
        Log.w(tag, "Cannot dispatch $eventName: React instance not active")
        return@runOnUiThread
      }
      if (id == View.NO_ID) {
        Log.w(tag, "Cannot dispatch $eventName: View has no ID")
        return@runOnUiThread
      }
      try {
        val event = Arguments.createMap()
        params.forEach { (key, value) ->
          when (value) {
            is Double -> event.putDouble(key, value)
            is Int -> event.putInt(key, value)
            is String -> event.putString(key, value)
            is Boolean -> event.putBoolean(key, value)
            is Long -> event.putDouble(key, value.toDouble())
            null -> event.putNull(key)
          }
        }
        reactContext.getJSModule(RCTEventEmitter::class.java)
          ?.receiveEvent(id, eventName, event)
      } catch (e: Exception) {
        Log.w(tag, "Failed to dispatch $eventName", e)
      }
    }
  }
}
