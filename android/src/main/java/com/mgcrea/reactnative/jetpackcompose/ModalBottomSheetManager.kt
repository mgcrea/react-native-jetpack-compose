package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.ModalBottomSheetManagerDelegate
import com.facebook.react.viewmanagers.ModalBottomSheetManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewGroupManager

internal class ModalBottomSheetManager :
  BaseComposeViewGroupManager<ModalBottomSheetViewDialog>(NAME, ::ModalBottomSheetViewDialog),
  ModalBottomSheetManagerInterface<ModalBottomSheetViewDialog> {

  override fun createDelegate(): ViewManagerDelegate<ModalBottomSheetViewDialog> =
    ModalBottomSheetManagerDelegate(this)

  override fun setVisible(view: ModalBottomSheetViewDialog, value: Boolean) {
    view.setVisible(value)
  }

  override fun setShowDragHandle(view: ModalBottomSheetViewDialog, value: Boolean) {
    view.showDragHandle = value
  }

  override fun setMaxHeightRatio(view: ModalBottomSheetViewDialog, value: Double) {
    view.maxHeightRatio = value
  }

  companion object {
    const val NAME = "ModalBottomSheet"
  }
}
