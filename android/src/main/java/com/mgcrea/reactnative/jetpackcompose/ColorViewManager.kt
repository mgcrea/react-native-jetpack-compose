package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.viewmanagers.ColorViewManagerDelegate
import com.facebook.react.viewmanagers.ColorViewManagerInterface

internal class ColorViewManager :
  SimpleViewManager<ColorView>(),
  ColorViewManagerInterface<ColorView> {

  private val delegate: ViewManagerDelegate<ColorView> = ColorViewManagerDelegate(this)

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): ColorView = ColorView(context)

  override fun getDelegate(): ViewManagerDelegate<ColorView> = delegate

  override fun setColor(view: ColorView, value: String?) {
    view.setColor(value)
  }

  companion object {
    const val NAME = "ColorView"
  }
}
