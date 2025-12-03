package com.mgcrea.reactnative.jetpackcompose

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.facebook.react.uimanager.JSPointerDispatcher
import com.facebook.react.uimanager.JSTouchDispatcher
import com.facebook.react.uimanager.RootView
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mgcrea.reactnative.jetpackcompose.core.DialogHostView

/**
 * Controller view that manages the bottom sheet content and implements RootView
 * for proper touch event handling. This view is set as the dialog's content view.
 *
 * Pattern from react-native-true-sheet: ReactViewGroup that implements RootView
 * and is used directly as the dialog's content view.
 */
@SuppressLint("ViewConstructor")
private class ModalBottomSheetController(
  private val reactContext: ThemedReactContext,
  private val onContentSizeChanged: (width: Int, height: Int) -> Unit
) :
  ReactViewGroup(reactContext),
  RootView {

  internal var eventDispatcher: EventDispatcher? = null
  private val jsTouchDispatcher = JSTouchDispatcher(this)
  private var jsPointerDispatcher: JSPointerDispatcher? = null

  private var lastWidth = 0
  private var lastHeight = 0

  init {
    jsPointerDispatcher = JSPointerDispatcher(this)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    if (w != lastWidth || h != lastHeight) {
      lastWidth = w
      lastHeight = h
      onContentSizeChanged(w, h)
    }
  }

  // RootView implementation
  override fun handleException(t: Throwable) {
    reactContext.reactApplicationContext.handleException(RuntimeException(t))
  }

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    eventDispatcher?.let {
      jsTouchDispatcher.handleTouchEvent(event, it, reactContext)
      jsPointerDispatcher?.handleMotionEvent(event, it, true)
    }
    return super.onInterceptTouchEvent(event)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    eventDispatcher?.let {
      jsTouchDispatcher.handleTouchEvent(event, it, reactContext)
      jsPointerDispatcher?.handleMotionEvent(event, it, false)
    }
    super.onTouchEvent(event)
    return true
  }

  override fun onChildStartedNativeGesture(childView: View?, ev: MotionEvent) {
    eventDispatcher?.let {
      jsTouchDispatcher.onChildStartedNativeGesture(ev, it)
      jsPointerDispatcher?.onChildStartedNativeGesture(childView, ev, it)
    }
  }

  override fun onChildEndedNativeGesture(childView: View, ev: MotionEvent) {
    eventDispatcher?.let { jsTouchDispatcher.onChildEndedNativeGesture(ev, it) }
    jsPointerDispatcher?.onChildEndedNativeGesture()
  }
}

/**
 * ModalBottomSheet implementation using Android's BottomSheetDialog.
 *
 * Architecture (following react-native-true-sheet pattern):
 * - This view extends DialogHostView (host stays hidden with visibility=GONE)
 * - Children are delegated to the controller
 * - The controller (ModalBottomSheetController) extends ReactViewGroup and implements RootView
 * - The controller is set as the dialog's content view
 * - This avoids LayoutParams casting issues because React Native views stay in a ReactViewGroup hierarchy
 */
@SuppressLint("ViewConstructor")
internal class ModalBottomSheetViewDialog(reactContext: ThemedReactContext) :
  DialogHostView(reactContext, TAG) {

  companion object {
    private const val TAG = "ModalBottomSheet"
  }

  // Controller that will be used as the dialog's content view
  private val controller = ModalBottomSheetController(reactContext) { _, height ->
    onContentHeightChanged(height)
  }
  private var contentHeight = 0

  private fun onContentHeightChanged(height: Int) {
    contentHeight = height
    // Update peekHeight if dialog is showing
    (dialog as? BottomSheetDialog)?.behavior?.let { behavior ->
      if (height > 0) {
        behavior.peekHeight = height
      }
    }
  }

  override fun createDialog(): Dialog {
    val activity = reactContext.currentActivity ?: throw IllegalStateException("No activity")

    return BottomSheetDialog(activity).apply {
      setOnDismissListener {
        if (isVisible) {
          markDismissed()
          dispatchEvent("topDismiss")
        }
      }
      behavior.apply {
        isFitToContents = true
        skipCollapsed = true
      }
    }
  }

  override fun onDialogShow() {
    (dialog as? BottomSheetDialog)?.apply {
      setContentView(controller)
      behavior.apply {
        if (contentHeight > 0) {
          peekHeight = contentHeight
        }
        state = BottomSheetBehavior.STATE_EXPANDED
      }
    }
  }

  // Route child operations to controller (following react-native-true-sheet pattern)
  override fun addView(child: View?, index: Int) {
    controller.addView(child, index)
  }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    controller.addView(child, index, params)
  }

  override fun removeView(view: View?) {
    controller.removeView(view)
  }

  override fun removeViewAt(index: Int) {
    controller.removeViewAt(index)
  }

  override fun removeAllViews() {
    controller.removeAllViews()
  }

  override fun getChildCount(): Int = controller.childCount

  override fun getChildAt(index: Int): View = controller.getChildAt(index)
}
