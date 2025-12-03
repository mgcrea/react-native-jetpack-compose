package com.mgcrea.reactnative.jetpackcompose.core

import android.app.Dialog
import android.util.Log
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.views.view.ReactViewGroup

/**
 * Base class for modal dialog host views.
 *
 * Implements the hidden host pattern:
 * - View stays mounted with visibility=GONE (never blocks touches)
 * - Content is rendered in a separate dialog window
 * - Single source of truth for visibility (React's `visible` prop)
 *
 * Subclasses implement [createDialog] to provide the specific dialog type
 * (BottomSheetDialog, AlertDialog, etc.) and optionally override
 * [onDialogShow] and [onDialogDismiss] for custom behavior.
 */
abstract class DialogHostView(
  protected val reactContext: ThemedReactContext,
  private val tag: String
) : ReactViewGroup(reactContext) {

  protected var dialog: Dialog? = null
  protected var isVisible = false
    private set

  /** Event dispatcher for Fabric events, set by ViewManager */
  var eventDispatcher: EventDispatcher? = null

  init {
    visibility = GONE  // Never blocks touches
  }

  /**
   * Sets the visibility of the dialog.
   * Called by ViewManager when the `visible` prop changes.
   */
  fun setVisible(value: Boolean) {
    if (value == isVisible) return
    isVisible = value
    UiThreadUtil.runOnUiThread {
      if (value) showDialog() else hideDialog()
    }
  }

  /**
   * Creates the dialog instance. Called once when first shown.
   * Subclasses should configure the dialog (content, listeners, etc.)
   */
  protected abstract fun createDialog(): Dialog

  /**
   * Called after the dialog is shown. Override to perform post-show setup.
   */
  protected open fun onDialogShow() {}

  /**
   * Called after the dialog is dismissed. Override for cleanup.
   */
  protected open fun onDialogDismiss() {}

  private fun showDialog() {
    val activity = reactContext.currentActivity ?: run {
      Log.w(tag, "Cannot show dialog: no current activity")
      return
    }
    dialog = dialog ?: createDialog()
    dialog?.show()
    onDialogShow()
  }

  private fun hideDialog() {
    dialog?.dismiss()
  }

  /**
   * Marks the dialog as dismissed from native side (e.g., user tapped outside).
   * Call this from dialog dismiss listeners before dispatching events.
   */
  protected fun markDismissed() {
    isVisible = false
  }

  /**
   * Dispatches an event to JavaScript using the Fabric event system.
   *
   * @param event The event to dispatch
   */
  protected fun dispatchEvent(event: Event<*>) {
    eventDispatcher?.dispatchEvent(event)
  }

  /**
   * Gets the surface ID for event dispatching.
   */
  protected fun getSurfaceId(): Int = UIManagerHelper.getSurfaceId(this)

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    // Clear event dispatcher first to prevent events during cleanup
    eventDispatcher = null
    // Mark as not visible to prevent dismiss events
    isVisible = false
    dialog?.dismiss()
    dialog = null
  }
}
