package com.mgcrea.reactnative.jetpackcompose.core

import android.app.Dialog
import android.util.Log
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.ThemedReactContext
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
  private val tag: String,
) : ReactViewGroup(reactContext), ReactHostView {

  protected var dialog: Dialog? = null
  protected var isVisible = false
    private set

  /** Set when the view is detached while showing, so the dialog comes back on reattach. */
  private var wasVisibleOnDetach = false

  override var eventDispatcher: EventDispatcher? = null
    set(value) {
      field = value
      onEventDispatcherChanged(value)
    }

  /** Flag to track if view has been dropped - used to prevent use-after-free crashes */
  @Volatile
  protected var isDetached: Boolean = false
    private set

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

  /**
   * Called whenever [eventDispatcher] changes. Override to forward the dispatcher to
   * a child RootView that lives in the dialog window.
   */
  protected open fun onEventDispatcherChanged(dispatcher: EventDispatcher?) {}

  private fun showDialog() {
    if (reactContext.currentActivity == null) {
      Log.w(tag, "Cannot show dialog: no current activity")
      return
    }
    val dialog = dialog ?: createDialog().also { dialog = it }
    dialog.show()
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
   * Called by ViewManager's onDropViewInstance before the view is destroyed.
   * Performs early cleanup to prevent use-after-free crashes in C++ props destructors.
   */
  override fun onDropInstance() {
    // Set detached flag first to prevent use-after-free crashes
    isDetached = true
    // Clear event dispatcher to prevent events during cleanup
    eventDispatcher = null
    isVisible = false
    dialog?.dismiss()
    dialog = null
  }

  /**
   * Dispatches an event to JavaScript using the Fabric event system.
   * Dispatched synchronously: dialog callbacks run from the dialog window, outside any
   * Compose apply pass, so the deferral [InlineComposeView] needs does not apply here.
   * Safely ignores dispatch if the view has been dropped to prevent use-after-free crashes.
   */
  override fun dispatchEvent(event: Event<*>) {
    if (isDetached) return
    eventDispatcher?.dispatchEvent(event)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    // Restore a dialog that was showing when the host was detached (e.g. navigation)
    if (wasVisibleOnDetach && !isDetached) {
      wasVisibleOnDetach = false
      setVisible(true)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    // The host may be reattached during navigation, so keep the dispatcher; only the
    // dialog window has to go. Mark not visible first so the dismiss listener stays quiet.
    wasVisibleOnDetach = isVisible
    isVisible = false
    dialog?.dismiss()
    dialog = null
  }
}
