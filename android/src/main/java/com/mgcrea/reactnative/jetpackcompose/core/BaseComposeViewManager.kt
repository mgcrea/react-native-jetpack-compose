package com.mgcrea.reactnative.jetpackcompose.core

import android.view.View
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewManagerDelegate

/**
 * Base ViewManager for leaf Compose views. Subclasses only implement the
 * codegen-generated `*ViewManagerInterface` setters and [createDelegate].
 */
abstract class BaseComposeViewManager<T>(
  private val viewName: String,
  private val factory: (ThemedReactContext) -> T,
) : SimpleViewManager<T>() where T : View, T : ReactHostView {

  /** Instantiates the codegen delegate; called once, lazily, after construction. */
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
