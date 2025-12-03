package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.ModalBottomSheetManagerDelegate
import com.facebook.react.viewmanagers.ModalBottomSheetManagerInterface

internal class ModalBottomSheetManager :
  ViewGroupManager<ModalBottomSheetViewDialog>(),
  ModalBottomSheetManagerInterface<ModalBottomSheetViewDialog> {

  private val delegate: ViewManagerDelegate<ModalBottomSheetViewDialog> =
    ModalBottomSheetManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): ModalBottomSheetViewDialog =
    ModalBottomSheetViewDialog(context)

  override fun getDelegate(): ViewManagerDelegate<ModalBottomSheetViewDialog> = delegate

  override fun addEventEmitters(reactContext: ThemedReactContext, view: ModalBottomSheetViewDialog) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }

  override fun setVisible(view: ModalBottomSheetViewDialog, value: Boolean) {
    view.setVisible(value)
  }

  override fun addView(parent: ModalBottomSheetViewDialog, child: android.view.View, index: Int) {
    parent.addView(child, index)
  }

  override fun removeViewAt(parent: ModalBottomSheetViewDialog, index: Int) {
    parent.removeViewAt(index)
  }

  override fun getChildCount(parent: ModalBottomSheetViewDialog): Int {
    return parent.childCount
  }

  override fun getChildAt(parent: ModalBottomSheetViewDialog, index: Int): android.view.View {
    return parent.getChildAt(index)
  }

  companion object {
    const val NAME = "ModalBottomSheet"
  }
}
