package com.mgcrea.reactnative.jetpackcompose

import android.graphics.Color
import android.util.Log
import android.view.View
import com.facebook.react.uimanager.ThemedReactContext

internal class ColorView(context: ThemedReactContext) : View(context) {

  fun setColor(value: String?) {
    val parsed = value?.let {
      try {
        Color.parseColor(it)
      } catch (e: IllegalArgumentException) {
        Log.w(TAG, "Invalid color value: '$it', using default black")
        null
      }
    } ?: DEFAULT_COLOR
    setBackgroundColor(parsed)
  }

  companion object {
    private const val TAG = "ColorView"
    private const val DEFAULT_COLOR: Int = 0xFF000000.toInt()
  }
}
