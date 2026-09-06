package com.mgcrea.reactnative.jetpackcompose.events

/**
 * Fabric event names. Each maps to an `on*` `DirectEventHandler` prop in the
 * TypeScript specs (codegen strips `on` and prefixes `top`).
 */
object EventNames {
  const val CONFIRM = "topConfirm"
  const val CANCEL = "topCancel"
  const val DATE_CHANGE = "topDateChange"
  const val TIME_CHANGE = "topTimeChange"
  const val VALUE_CHANGE = "topValueChange"
  const val VALUES_CHANGE = "topValuesChange"
  const val ITEM_SELECT = "topItemSelect"
  const val DISMISS = "topDismiss"
  const val TEXT_FIELD_CHANGE = "topTextFieldChange"
  const val TEXT_FIELD_FOCUS = "topTextFieldFocus"
  const val TEXT_FIELD_BLUR = "topTextFieldBlur"
  const val SUBMIT_EDITING = "topSubmitEditing"
  const val TRAILING_ICON_PRESS = "topTrailingIconPress"
}
