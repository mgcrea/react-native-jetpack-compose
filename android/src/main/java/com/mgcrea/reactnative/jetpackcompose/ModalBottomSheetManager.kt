package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.ModalBottomSheetManagerDelegate
import com.facebook.react.viewmanagers.ModalBottomSheetManagerInterface

internal class ModalBottomSheetManager :
  ViewGroupManager<ModalBottomSheetView>(),
  ModalBottomSheetManagerInterface<ModalBottomSheetView> {

  private val delegate: ViewManagerDelegate<ModalBottomSheetView> =
    ModalBottomSheetManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): ModalBottomSheetView =
    ModalBottomSheetView(context)

  override fun getDelegate(): ViewManagerDelegate<ModalBottomSheetView> = delegate

  override fun setVisible(view: ModalBottomSheetView, value: Boolean) {
    view.setVisible(value)
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> {
    return MapBuilder.builder<String, Any>()
      .put(
        "topDismiss",
        MapBuilder.of("registrationName", "onDismiss")
      )
      .build()
  }

  override fun addView(parent: ModalBottomSheetView, child: android.view.View, index: Int) {
    parent.addView(child, index)
  }

  override fun removeViewAt(parent: ModalBottomSheetView, index: Int) {
    parent.removeViewAt(index)
  }

  override fun getChildCount(parent: ModalBottomSheetView): Int {
    return parent.childCount
  }

  override fun getChildAt(parent: ModalBottomSheetView, index: Int): android.view.View {
    return parent.getChildAt(index)
  }

  companion object {
    const val NAME = "ModalBottomSheet"
  }
}
