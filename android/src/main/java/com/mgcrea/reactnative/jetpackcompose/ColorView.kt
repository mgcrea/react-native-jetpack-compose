package com.mgcrea.reactnative.jetpackcompose

import android.graphics.Color
import android.view.View
import com.facebook.react.uimanager.ThemedReactContext

internal class ColorView(context: ThemedReactContext) : View(context) {

  fun setColor(value: String?) {
    val parsed = value?.let {
      try {
        Color.parseColor(it)
      } catch (_: IllegalArgumentException) {
        null
      }
    } ?: DEFAULT_COLOR
    setBackgroundColor(parsed)
  }

  companion object {
    private const val DEFAULT_COLOR: Int = 0xFF000000.toInt()
  }
}
