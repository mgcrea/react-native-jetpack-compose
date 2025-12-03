package com.mgcrea.reactnative.jetpackcompose

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.facebook.react.views.view.ReactViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Custom ReactViewGroup that ensures all children have FrameLayout.LayoutParams.
 * This prevents ClassCastException when used inside BottomSheetDialog's CoordinatorLayout/FrameLayout hierarchy.
 *
 * Note: FrameLayout.LayoutParams extends MarginLayoutParams, so this satisfies both requirements.
 */
private class SafeReactViewGroup(context: ThemedReactContext) : ReactViewGroup(context) {

  override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams?) {
    // Ensure the child has FrameLayout.LayoutParams
    val safeParams = when (params) {
      is FrameLayout.LayoutParams -> params
      is MarginLayoutParams -> FrameLayout.LayoutParams(params)
      null -> FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
      else -> FrameLayout.LayoutParams(params)
    }
    super.addView(child, index, safeParams)
  }

  override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
    return p is FrameLayout.LayoutParams
  }

  override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): FrameLayout.LayoutParams {
    return when (lp) {
      is FrameLayout.LayoutParams -> lp
      is MarginLayoutParams -> FrameLayout.LayoutParams(lp)
      null -> FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
      else -> FrameLayout.LayoutParams(lp)
    }
  }

  override fun generateDefaultLayoutParams(): LayoutParams {
    return FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
  }
}

/**
 * Alternative ModalBottomSheet implementation using Android's BottomSheetDialog
 * instead of Jetpack Compose's ModalBottomSheet.
 *
 * This avoids the LayoutParams casting issues that occur when embedding
 * React Native views inside Compose's AndroidView.
 */
internal class ModalBottomSheetViewDialog(private val reactContext: ThemedReactContext) :
  FrameLayout(reactContext) {

  companion object {
    private const val TAG = "ModalBottomSheetDialog"
  }

  private val contentContainer = SafeReactViewGroup(reactContext)
  private var bottomSheetDialog: BottomSheetDialog? = null
  private var isVisible = false

  init {
    // Keep contentContainer as a child when dialog is not shown
    super.addView(
      contentContainer,
      FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    )
  }

  fun setVisible(value: Boolean) {
    if (value == isVisible) return
    isVisible = value

    UiThreadUtil.runOnUiThread {
      if (value) {
        showBottomSheet()
      } else {
        hideBottomSheet()
      }
    }
  }

  private fun showBottomSheet() {
    val activity = reactContext.currentActivity ?: run {
      Log.w(TAG, "Cannot show bottom sheet: no current activity")
      return
    }

    val dialog = bottomSheetDialog ?: createDialog()

    // Reparent contentContainer to dialog
    (contentContainer.parent as? ViewGroup)?.removeView(contentContainer)

    // Ensure contentContainer has proper LayoutParams for the dialog
    contentContainer.layoutParams = FrameLayout.LayoutParams(
      LayoutParams.MATCH_PARENT,
      LayoutParams.WRAP_CONTENT
    )

    dialog.setContentView(contentContainer)

    dialog.show()

    // Configure behavior after show
    dialog.behavior.apply {
      state = BottomSheetBehavior.STATE_EXPANDED
      skipCollapsed = true
    }
  }

  private fun hideBottomSheet() {
    bottomSheetDialog?.dismiss()
  }

  private fun createDialog(): BottomSheetDialog {
    val activity = reactContext.currentActivity ?: throw IllegalStateException("No activity")

    return BottomSheetDialog(activity).apply {
      setOnDismissListener {
        // Reparent contentContainer back to this view
        (contentContainer.parent as? ViewGroup)?.removeView(contentContainer)
        this@ModalBottomSheetViewDialog.addView(contentContainer)

        if (isVisible) {
          isVisible = false
          dispatchDismissEvent()
        }
      }
      bottomSheetDialog = this
    }
  }

  private fun dispatchDismissEvent() {
    UiThreadUtil.runOnUiThread {
      if (!reactContext.hasActiveReactInstance()) {
        Log.w(TAG, "Cannot dispatch dismiss event: React instance is not active")
        return@runOnUiThread
      }
      if (id == View.NO_ID) {
        Log.w(TAG, "Cannot dispatch dismiss event: View has no ID")
        return@runOnUiThread
      }
      try {
        val event = Arguments.createMap()
        reactContext
          .getJSModule(RCTEventEmitter::class.java)
          ?.receiveEvent(id, "topDismiss", event)
      } catch (e: Exception) {
        Log.w(TAG, "Failed to dispatch dismiss event", e)
      }
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    bottomSheetDialog?.dismiss()
    bottomSheetDialog = null
  }

  // Route child operations to contentContainer
  override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams?) {
    if (child === contentContainer) {
      super.addView(child, index, params)
    } else {
      val safeIndex = if (index < 0 || index > contentContainer.childCount) {
        contentContainer.childCount
      } else {
        index
      }
      contentContainer.addView(child, safeIndex, params)
    }
  }

  override fun addView(child: View, index: Int) {
    addView(child, index, child.layoutParams)
  }

  override fun removeView(view: View) {
    if (view === contentContainer) {
      super.removeView(view)
    } else {
      contentContainer.removeView(view)
    }
  }

  override fun removeViewAt(index: Int) {
    contentContainer.removeViewAt(index)
  }

  override fun removeAllViews() {
    contentContainer.removeAllViews()
  }

  override fun getChildCount(): Int = contentContainer.childCount

  override fun getChildAt(index: Int): View = contentContainer.getChildAt(index)
}
