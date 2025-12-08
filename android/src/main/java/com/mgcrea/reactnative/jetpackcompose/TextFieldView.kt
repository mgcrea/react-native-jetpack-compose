package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.events.TextFieldChangeEvent
import com.mgcrea.reactnative.jetpackcompose.events.TextFieldBlurEvent
import com.mgcrea.reactnative.jetpackcompose.events.TextFieldFocusEvent
import com.mgcrea.reactnative.jetpackcompose.events.TextFieldSubmitEvent
import com.mgcrea.reactnative.jetpackcompose.events.TextFieldTrailingIconPressEvent

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

  // New state for keyboard and icons
  private val _keyboardType = mutableStateOf("default")
  private val _returnKeyType = mutableStateOf("done")
  private val _autoCapitalize = mutableStateOf("sentences")
  private val _autoCorrect = mutableStateOf(true)
  private val _leadingIcon = mutableStateOf<String?>(null)
  private val _trailingIcon = mutableStateOf<String?>(null)
  private val _showCounter = mutableStateOf(true)

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

  fun setKeyboardType(value: String?) {
    _keyboardType.value = value ?: "default"
  }

  fun setReturnKeyType(value: String?) {
    _returnKeyType.value = value ?: "done"
  }

  fun setAutoCapitalize(value: String?) {
    _autoCapitalize.value = value ?: "sentences"
  }

  fun setAutoCorrect(value: Boolean) {
    _autoCorrect.value = value
  }

  fun setLeadingIcon(value: String?) {
    _leadingIcon.value = value
  }

  fun setTrailingIcon(value: String?) {
    _trailingIcon.value = value
  }

  fun setShowCounter(value: Boolean) {
    _showCounter.value = value
  }

  private fun getKeyboardType(type: String): KeyboardType = when (type) {
    "email" -> KeyboardType.Email
    "number" -> KeyboardType.Number
    "phone" -> KeyboardType.Phone
    "decimal" -> KeyboardType.Decimal
    "url" -> KeyboardType.Uri
    else -> KeyboardType.Text
  }

  private fun getImeAction(type: String): ImeAction = when (type) {
    "go" -> ImeAction.Go
    "next" -> ImeAction.Next
    "search" -> ImeAction.Search
    "send" -> ImeAction.Send
    else -> ImeAction.Done
  }

  private fun getCapitalization(type: String): KeyboardCapitalization = when (type) {
    "none" -> KeyboardCapitalization.None
    "words" -> KeyboardCapitalization.Words
    "characters" -> KeyboardCapitalization.Characters
    else -> KeyboardCapitalization.Sentences
  }

  private fun getIcon(name: String?): ImageVector? = when (name?.lowercase()) {
    "search" -> Icons.Default.Search
    "email" -> Icons.Default.Email
    "phone" -> Icons.Default.Phone
    "person" -> Icons.Default.Person
    "lock" -> Icons.Default.Lock
    "clear" -> Icons.Default.Clear
    "close" -> Icons.Default.Close
    "check" -> Icons.Default.Check
    "edit" -> Icons.Default.Edit
    "info" -> Icons.Default.Info
    "warning" -> Icons.Default.Warning
    else -> null
  }

  @Composable
  override fun ComposeContent() {
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
    val keyboardType = _keyboardType.value
    val returnKeyType = _returnKeyType.value
    val autoCapitalize = _autoCapitalize.value
    val autoCorrect = _autoCorrect.value
    val leadingIconName = _leadingIcon.value
    val trailingIconName = _trailingIcon.value
    val showCounter = _showCounter.value

    val onSubmit = {
      dispatchEvent(TextFieldSubmitEvent(getSurfaceId(), id))
    }

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
      supportingText = if (helperText != null || (showCounter && maxLength != null)) {
        {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(helperText ?: "")
            if (showCounter && maxLength != null) {
              Text("${value.length}/$maxLength")
            }
          }
        }
      } else null,
      visualTransformation = if (secureTextEntry) {
        PasswordVisualTransformation()
      } else {
        VisualTransformation.None
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = getKeyboardType(keyboardType),
        imeAction = getImeAction(returnKeyType),
        capitalization = getCapitalization(autoCapitalize),
        autoCorrectEnabled = autoCorrect
      ),
      keyboardActions = KeyboardActions(
        onDone = { onSubmit() },
        onGo = { onSubmit() },
        onNext = { onSubmit() },
        onSearch = { onSubmit() },
        onSend = { onSubmit() }
      ),
      leadingIcon = leadingIconName?.let { iconName ->
        getIcon(iconName)?.let { icon ->
          { Icon(icon, contentDescription = iconName) }
        }
      },
      trailingIcon = trailingIconName?.let { iconName ->
        getIcon(iconName)?.let { icon ->
          {
            IconButton(onClick = {
              dispatchEvent(TextFieldTrailingIconPressEvent(getSurfaceId(), id))
            }) {
              Icon(icon, contentDescription = iconName)
            }
          }
        }
      },
    )
  }
}
