package com.mgcrea.reactnative.jetpackcompose

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.facebook.react.uimanager.JSPointerDispatcher
import com.facebook.react.uimanager.JSTouchDispatcher
import com.facebook.react.uimanager.RootView
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mgcrea.reactnative.jetpackcompose.core.DialogHostView
import com.mgcrea.reactnative.jetpackcompose.events.DismissEvent

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
  private var showDragHandle = true
  private var maxHeightRatio = 0.9

  fun setShowDragHandle(value: Boolean) {
    showDragHandle = value
  }

  fun setMaxHeightRatio(value: Double) {
    maxHeightRatio = value
  }

  private fun dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      dp,
      reactContext.resources.displayMetrics
    ).toInt()
  }

  private fun createDragHandle(): View {
    val handleWidth = dpToPx(32f)
    val handleHeight = dpToPx(4f)
    val verticalPadding = dpToPx(12f)

    // Create the drag handle shape
    val handleDrawable = GradientDrawable().apply {
      shape = GradientDrawable.RECTANGLE
      cornerRadius = handleHeight / 2f
      setColor(0x66808080) // Semi-transparent gray
    }

    // Create the handle view
    val handleView = View(reactContext).apply {
      layoutParams = LinearLayout.LayoutParams(handleWidth, handleHeight).apply {
        gravity = Gravity.CENTER_HORIZONTAL
        topMargin = verticalPadding
        bottomMargin = verticalPadding
      }
      background = handleDrawable
    }

    return handleView
  }

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
          dispatchEvent(DismissEvent(getSurfaceId(), id))
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
      // Calculate max height
      val displayMetrics = reactContext.resources.displayMetrics
      val maxHeight = (displayMetrics.heightPixels * maxHeightRatio).toInt()

      // Create container with drag handle and content
      val container = LinearLayout(reactContext).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = FrameLayout.LayoutParams(
          FrameLayout.LayoutParams.MATCH_PARENT,
          FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
          this.height = maxHeight.coerceAtMost(FrameLayout.LayoutParams.WRAP_CONTENT)
        }
      }

      // Add drag handle if enabled
      if (showDragHandle) {
        container.addView(createDragHandle())
      }

      // Add controller (content)
      (controller.parent as? ViewGroup)?.removeView(controller)
      container.addView(controller, LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).apply {
        weight = 1f
      })

      setContentView(container)

      // Limit the bottom sheet max height
      behavior.apply {
        if (contentHeight > 0) {
          peekHeight = contentHeight.coerceAtMost(maxHeight)
        }
        this.maxHeight = maxHeight
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
