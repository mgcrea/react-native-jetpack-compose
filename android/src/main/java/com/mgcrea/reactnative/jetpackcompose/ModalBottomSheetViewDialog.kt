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
import com.mgcrea.reactnative.jetpackcompose.core.emitEvent
import com.mgcrea.reactnative.jetpackcompose.events.EventNames

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
  private val onContentSizeChanged: (width: Int, height: Int) -> Unit,
) : ReactViewGroup(reactContext), RootView {

  /** Forwarded from the host; without it no touch inside the sheet reaches JS. */
  var eventDispatcher: EventDispatcher? = null
  private val jsTouchDispatcher = JSTouchDispatcher(this)
  private val jsPointerDispatcher = JSPointerDispatcher(this)

  private var lastWidth = 0
  private var lastHeight = 0

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
      jsPointerDispatcher.handleMotionEvent(event, it, true)
    }
    return super.onInterceptTouchEvent(event)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    eventDispatcher?.let {
      jsTouchDispatcher.handleTouchEvent(event, it, reactContext)
      jsPointerDispatcher.handleMotionEvent(event, it, false)
    }
    super.onTouchEvent(event)
    return true
  }

  override fun onChildStartedNativeGesture(childView: View?, ev: MotionEvent) {
    eventDispatcher?.let {
      jsTouchDispatcher.onChildStartedNativeGesture(ev, it)
      jsPointerDispatcher.onChildStartedNativeGesture(childView, ev, it)
    }
  }

  override fun onChildEndedNativeGesture(childView: View, ev: MotionEvent) {
    eventDispatcher?.let { jsTouchDispatcher.onChildEndedNativeGesture(ev, it) }
    jsPointerDispatcher.onChildEndedNativeGesture()
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

  var showDragHandle: Boolean = true
    set(value) {
      field = value
      dragHandle.visibility = if (value) View.VISIBLE else View.GONE
    }

  var maxHeightRatio: Double = 0.9

  // Built once; re-parented into each dialog window as needed
  private val dragHandle: View by lazy { createDragHandle() }
  private val container: LinearLayout by lazy {
    LinearLayout(reactContext).apply {
      orientation = LinearLayout.VERTICAL
      layoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT,
      )
      addView(dragHandle)
      addView(
        controller,
        LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.WRAP_CONTENT,
        ).apply { weight = 1f },
      )
    }
  }

  override fun onEventDispatcherChanged(dispatcher: EventDispatcher?) {
    controller.eventDispatcher = dispatcher
  }

  private fun dpToPx(dp: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, reactContext.resources.displayMetrics).toInt()

  private fun createDragHandle(): View {
    val handleWidth = dpToPx(32f)
    val handleHeight = dpToPx(4f)
    val verticalPadding = dpToPx(12f)

    val handleDrawable = GradientDrawable().apply {
      shape = GradientDrawable.RECTANGLE
      cornerRadius = handleHeight / 2f
      setColor(0x66808080) // Semi-transparent gray
    }

    return View(reactContext).apply {
      layoutParams = LinearLayout.LayoutParams(handleWidth, handleHeight).apply {
        gravity = Gravity.CENTER_HORIZONTAL
        topMargin = verticalPadding
        bottomMargin = verticalPadding
      }
      background = handleDrawable
      visibility = if (showDragHandle) View.VISIBLE else View.GONE
    }
  }

  private fun maxSheetHeightPx(): Int =
    (reactContext.resources.displayMetrics.heightPixels * maxHeightRatio).toInt()

  private fun onContentHeightChanged(height: Int) {
    contentHeight = height
    // Update peekHeight if dialog is showing
    if (height > 0) {
      (dialog as? BottomSheetDialog)?.behavior?.peekHeight = height.coerceAtMost(maxSheetHeightPx())
    }
  }

  override fun createDialog(): Dialog {
    val activity = reactContext.currentActivity ?: throw IllegalStateException("No activity")

    return BottomSheetDialog(activity).apply {
      setOnDismissListener {
        if (isVisible) {
          markDismissed()
          emitEvent(EventNames.DISMISS)
        }
      }
      // A previous dialog window may still own the container
      (container.parent as? ViewGroup)?.removeView(container)
      setContentView(container)
      behavior.apply {
        isFitToContents = true
        skipCollapsed = true
      }
    }
  }

  override fun onDialogShow() {
    val maxHeight = maxSheetHeightPx()
    (dialog as? BottomSheetDialog)?.behavior?.apply {
      if (contentHeight > 0) {
        peekHeight = contentHeight.coerceAtMost(maxHeight)
      }
      this.maxHeight = maxHeight
      state = BottomSheetBehavior.STATE_EXPANDED
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

  override fun getChildAt(index: Int): View? = controller.getChildAt(index)
}
