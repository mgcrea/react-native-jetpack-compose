package com.mgcrea.reactnative.jetpackcompose

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.UiThreadUtil
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.facebook.react.views.view.ReactViewGroup

@OptIn(ExperimentalMaterial3Api::class)
internal class ModalBottomSheetView(private val reactContext: ThemedReactContext) :
  FrameLayout(reactContext) {

  private val _isVisible = mutableStateOf(false)

  private val contentContainer = ReactViewGroup(reactContext)

  private val contentHost = FrameLayout(reactContext).apply {
    layoutParams = LayoutParams(
      LayoutParams.MATCH_PARENT,
      LayoutParams.WRAP_CONTENT
    )
    addView(
      contentContainer,
      LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
      )
    )
  }

  private val composeView = ComposeView(reactContext)

  init {
    composeView.setContent {
      ModalBottomSheetContent()
    }
    super.addView(composeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
  }

  fun setVisible(value: Boolean) {
    _isVisible.value = value
  }

  private fun dispatchDismissEvent() {
    UiThreadUtil.runOnUiThread {
      val event: WritableMap = Arguments.createMap()
      reactContext
        .getJSModule(RCTEventEmitter::class.java)
        .receiveEvent(id, "topDismiss", event)
    }
  }

  @Composable
  private fun ModalBottomSheetContent() {
    val sheetState = rememberModalBottomSheetState()
    val isVisible = _isVisible.value

    LaunchedEffect(isVisible) {
      if (isVisible) {
        sheetState.show()
      } else {
        sheetState.hide()
      }
    }

    if (isVisible) {
      ModalBottomSheet(
        onDismissRequest = {
          _isVisible.value = false
          dispatchDismissEvent()
        },
        sheetState = sheetState
      ) {
        AndroidView(
          factory = { contentHost },
          modifier = Modifier.fillMaxWidth(),
          update = { host ->
            if (host !== contentHost) {
              host.removeAllViews()
              host.addView(contentHost)
            } else {
              requestContainerLayout()
            }
          }
        )
      }
    }
  }

  override fun addView(child: android.view.View, index: Int, params: ViewGroup.LayoutParams?) {
    if (child === composeView) {
      super.addView(child, index, params)
    } else {
      val layoutParams = params ?: LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
      )
      contentContainer.addView(child, index, layoutParams)
      requestContainerLayout()
    }
  }

  override fun addView(child: android.view.View, index: Int) {
    addView(child, index, child.layoutParams)
  }

  override fun removeView(view: android.view.View) {
    if (view === composeView) {
      super.removeView(view)
    } else {
      contentContainer.removeView(view)
      requestContainerLayout()
    }
  }

  override fun removeViewAt(index: Int) {
    contentContainer.removeViewAt(index)
    requestContainerLayout()
  }

  override fun removeAllViews() {
    contentContainer.removeAllViews()
    requestContainerLayout()
  }

  override fun getChildCount(): Int {
    return contentContainer.childCount
  }

  override fun getChildAt(index: Int): android.view.View {
    return contentContainer.getChildAt(index)
  }

  private fun requestContainerLayout() {
    UiThreadUtil.runOnUiThread {
      contentContainer.requestLayout()
    }
  }
}
