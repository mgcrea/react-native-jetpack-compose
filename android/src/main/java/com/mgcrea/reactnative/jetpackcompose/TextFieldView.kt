package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.events.TextFieldChangeEvent
import com.mgcrea.reactnative.jetpackcompose.events.TextFieldBlurEvent
import com.mgcrea.reactnative.jetpackcompose.events.TextFieldFocusEvent

internal class TextFieldView(reactContext: ThemedReactContext) :
  InlineComposeView(reactContext, TAG) {

  companion object {
    private const val TAG = "TextFieldView"
  }

  // State backing for Compose
  private val _value = mutableStateOf("")
  private val _label = mutableStateOf<String?>(null)
  private val _placeholder = mutableStateOf<String?>(null)
  private val _disabled = mutableStateOf(false)
  private val _editable = mutableStateOf(true)
  private val _multiline = mutableStateOf(false)
  private val _maxLength = mutableStateOf<Int?>(null)
  private val _secureTextEntry = mutableStateOf(false)
  private val _error = mutableStateOf(false)
  private val _helperText = mutableStateOf<String?>(null)
  private val _isFocused = mutableStateOf(false)

  // Property setters called by ViewManager
  fun setValue(value: String?) {
    _value.value = value ?: ""
  }

  fun setLabel(value: String?) {
    _label.value = value
  }

  fun setPlaceholder(value: String?) {
    _placeholder.value = value
  }

  fun setDisabled(value: Boolean) {
    _disabled.value = value
  }

  fun setEditable(value: Boolean) {
    _editable.value = value
  }

  fun setMultiline(value: Boolean) {
    _multiline.value = value
  }

  fun setMaxLength(value: Double?) {
    _maxLength.value = value?.toInt()
  }

  fun setSecureTextEntry(value: Boolean) {
    _secureTextEntry.value = value
  }

  fun setError(value: Boolean) {
    _error.value = value
  }

  fun setHelperText(value: String?) {
    _helperText.value = value
  }

  @Composable
  override fun Content() {
    val value = _value.value
    val label = _label.value
    val placeholder = _placeholder.value
    val disabled = _disabled.value
    val editable = _editable.value
    val multiline = _multiline.value
    val maxLength = _maxLength.value
    val secureTextEntry = _secureTextEntry.value
    val error = _error.value
    val helperText = _helperText.value

    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          val wasFocused = _isFocused.value
          _isFocused.value = focusState.isFocused

          if (focusState.isFocused && !wasFocused) {
            dispatchEvent(TextFieldFocusEvent(getSurfaceId(), id))
          } else if (!focusState.isFocused && wasFocused) {
            dispatchEvent(TextFieldBlurEvent(getSurfaceId(), id))
          }
        },
      value = value,
      onValueChange = { newValue ->
        // Apply maxLength constraint
        val constrainedValue = if (maxLength != null && newValue.length > maxLength) {
          newValue.take(maxLength)
        } else {
          newValue
        }

        if (constrainedValue != value) {
          // Update local state immediately to preserve cursor position
          _value.value = constrainedValue
          // Then notify JS
          dispatchEvent(TextFieldChangeEvent(getSurfaceId(), id, constrainedValue))
        }
      },
      enabled = !disabled,
      readOnly = !editable,
      singleLine = !multiline,
      minLines = if (multiline) 3 else 1,
      isError = error,
      label = label?.let { { Text(it) } },
      placeholder = placeholder?.let { { Text(it, maxLines = 1) } },
      supportingText = helperText?.let { { Text(it) } },
      visualTransformation = if (secureTextEntry) {
        PasswordVisualTransformation()
      } else {
        VisualTransformation.None
      },
    )
  }
}
