package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.SegmentedButtonViewManagerDelegate
import com.facebook.react.viewmanagers.SegmentedButtonViewManagerInterface
import com.mgcrea.reactnative.jetpackcompose.core.BaseComposeViewManager
import com.mgcrea.reactnative.jetpackcompose.core.toOptions
import com.mgcrea.reactnative.jetpackcompose.core.toStringSet

internal class SegmentedButtonViewManager :
  BaseComposeViewManager<SegmentedButtonView>(NAME, ::SegmentedButtonView),
  SegmentedButtonViewManagerInterface<SegmentedButtonView> {

  override fun createDelegate(): ViewManagerDelegate<SegmentedButtonView> = SegmentedButtonViewManagerDelegate(this)

  override fun setSegments(view: SegmentedButtonView, value: ReadableArray?) {
    view.segments = value.toOptions()
  }

  override fun setSelectedValue(view: SegmentedButtonView, value: String?) {
    view.selectedValue = value
  }

  override fun setSelectedValues(view: SegmentedButtonView, value: ReadableArray?) {
    view.selectedValues = value.toStringSet()
  }

  override fun setMultiSelect(view: SegmentedButtonView, value: Boolean) {
    view.multiSelect = value
  }

  override fun setDisabled(view: SegmentedButtonView, value: Boolean) {
    view.disabled = value
  }

  companion object {
    const val NAME = "SegmentedButtonView"
  }
}
