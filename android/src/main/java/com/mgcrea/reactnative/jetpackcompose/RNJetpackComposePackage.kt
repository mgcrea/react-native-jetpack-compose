package com.mgcrea.reactnative.jetpackcompose

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

// The plain ReactPackage API is deprecated in newer React Native releases in favour of
// BaseReactPackage, but it is the one available across the whole supported peer range.
@Suppress("OVERRIDE_DEPRECATION")
class RNJetpackComposePackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> =
    emptyList()

  override fun createViewManagers(
    reactContext: ReactApplicationContext,
  ): List<ViewManager<*, *>> = listOf(
    ModalBottomSheetManager(),
    DatePickerViewManager(),
    DateRangePickerViewManager(),
    TimePickerViewManager(),
    TimeRangePickerViewManager(),
    PickerViewManager(),
    SegmentedButtonViewManager(),
    SheetPickerViewManager(),
    TextFieldViewManager(),
  )
}
