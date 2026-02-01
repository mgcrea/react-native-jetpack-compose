package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.SegmentedButtonViewManagerDelegate
import com.facebook.react.viewmanagers.SegmentedButtonViewManagerInterface

@ReactModule(name = SegmentedButtonViewManager.NAME)
internal class SegmentedButtonViewManager :
  SimpleViewManager<SegmentedButtonView>(),
  SegmentedButtonViewManagerInterface<SegmentedButtonView> {

  private val delegate: ViewManagerDelegate<SegmentedButtonView> =
    SegmentedButtonViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): SegmentedButtonView =
    SegmentedButtonView(context)

  override fun getDelegate(): ViewManagerDelegate<SegmentedButtonView> = delegate

  override fun onDropViewInstance(view: SegmentedButtonView) {
    super.onDropViewInstance(view)
    view.onDropInstance()
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: SegmentedButtonView) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }

  override fun setSegments(view: SegmentedButtonView, value: ReadableArray?) {
    view.setSegments(value)
  }

  override fun setSelectedValue(view: SegmentedButtonView, value: String?) {
    view.setSelectedValue(value)
  }

  override fun setSelectedValues(view: SegmentedButtonView, value: ReadableArray?) {
    view.setSelectedValues(value)
  }

  override fun setMultiSelect(view: SegmentedButtonView, value: Boolean) {
    view.setMultiSelect(value)
  }

  override fun setDisabled(view: SegmentedButtonView, value: Boolean) {
    view.setDisabled(value)
  }

  companion object {
    const val NAME = "SegmentedButtonView"
  }
}
