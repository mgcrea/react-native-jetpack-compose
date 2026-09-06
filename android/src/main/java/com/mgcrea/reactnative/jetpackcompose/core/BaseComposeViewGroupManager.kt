package com.mgcrea.reactnative.jetpackcompose.core

import android.view.ViewGroup
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ViewManagerDelegate

/**
 * Base ViewManager for host views that accept React children (dialog hosts).
 * Mirrors [BaseComposeViewManager] on top of [ViewGroupManager].
 */
abstract class BaseComposeViewGroupManager<T>(
  private val viewName: String,
  private val factory: (ThemedReactContext) -> T,
) : ViewGroupManager<T>() where T : ViewGroup, T : ReactHostView {

  protected abstract fun createDelegate(): ViewManagerDelegate<T>

  private val delegateInstance: ViewManagerDelegate<T> by lazy { createDelegate() }

  final override fun getName(): String = viewName

  final override fun createViewInstance(context: ThemedReactContext): T = factory(context)

  final override fun getDelegate(): ViewManagerDelegate<T> = delegateInstance

  override fun onDropViewInstance(view: T) {
    super.onDropViewInstance(view)
    view.onDropInstance()
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: T) {
    view.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
  }
}
