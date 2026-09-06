package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.core.emitEvent
import com.mgcrea.reactnative.jetpackcompose.events.EventNames
import com.mgcrea.reactnative.jetpackcompose.ui.MaterialIcons

/**
 * Material3 [OutlinedTextField] driven by React Native props.
 */
internal class TextFieldView(reactContext: ThemedReactContext) : InlineComposeView(reactContext) {

  var value: String by mutableStateOf("")
  var label: String? by mutableStateOf(null)
  var placeholder: String? by mutableStateOf(null)
  var disabled: Boolean by mutableStateOf(false)
  var editable: Boolean by mutableStateOf(true)
  var multiline: Boolean by mutableStateOf(false)
  var maxLength: Int? by mutableStateOf(null)
  var secureTextEntry: Boolean by mutableStateOf(false)
  var error: Boolean by mutableStateOf(false)
  var helperText: String? by mutableStateOf(null)
  var keyboardType: String by mutableStateOf("default")
  var returnKeyType: String by mutableStateOf("done")
  var autoCapitalize: String by mutableStateOf("sentences")
  var autoCorrect: Boolean by mutableStateOf(true)
  var leadingIcon: String? by mutableStateOf(null)
  var trailingIcon: String? by mutableStateOf(null)
  var showCounter: Boolean by mutableStateOf(true)

  /** Last focus state seen, used to emit focus/blur transitions only once. */
  private var isFocused = false

  // Built once per view: they only reference `this`
  private val onSubmit: () -> Unit = { emitEvent(EventNames.SUBMIT_EDITING) }
  private val keyboardActions = KeyboardActions(
    onDone = { onSubmit() },
    onGo = { onSubmit() },
    onNext = { onSubmit() },
    onSearch = { onSubmit() },
    onSend = { onSubmit() },
  )
  private val passwordTransformation = PasswordVisualTransformation()

  @Composable
  override fun ComposeContent() {
    val value = value
    val maxLength = maxLength
    val helperText = helperText
    val showCounter = showCounter
    val keyboardType = keyboardType
    val returnKeyType = returnKeyType
    val autoCapitalize = autoCapitalize
    val autoCorrect = autoCorrect
    val leadingIconName = leadingIcon
    val trailingIconName = trailingIcon

    val keyboardOptions = remember(keyboardType, returnKeyType, autoCapitalize, autoCorrect) {
      KeyboardOptions(
        keyboardType = keyboardTypeOf(keyboardType),
        imeAction = imeActionOf(returnKeyType),
        capitalization = capitalizationOf(autoCapitalize),
        autoCorrectEnabled = autoCorrect,
      )
    }
    val leadingIconVector = remember(leadingIconName) { MaterialIcons.resolve(leadingIconName) }
    val trailingIconVector = remember(trailingIconName) { MaterialIcons.resolve(trailingIconName) }

    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          val wasFocused = isFocused
          isFocused = focusState.isFocused
          if (focusState.isFocused && !wasFocused) {
            emitEvent(EventNames.TEXT_FIELD_FOCUS)
          } else if (!focusState.isFocused && wasFocused) {
            emitEvent(EventNames.TEXT_FIELD_BLUR)
          }
        },
      value = value,
      onValueChange = { newValue ->
        val constrainedValue = if (maxLength != null && newValue.length > maxLength) {
          newValue.take(maxLength)
        } else {
          newValue
        }
        if (constrainedValue != value) {
          // Update local state immediately to preserve cursor position, then notify JS
          this.value = constrainedValue
          emitEvent(EventNames.TEXT_FIELD_CHANGE) { putString("text", constrainedValue) }
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
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(helperText ?: "")
            if (showCounter && maxLength != null) {
              Text("${value.length}/$maxLength")
            }
          }
        }
      } else {
        null
      },
      visualTransformation = if (secureTextEntry) passwordTransformation else VisualTransformation.None,
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      leadingIcon = leadingIconVector?.let { icon ->
        { Icon(icon, contentDescription = leadingIconName) }
      },
      trailingIcon = trailingIconVector?.let { icon ->
        {
          IconButton(onClick = { emitEvent(EventNames.TRAILING_ICON_PRESS) }) {
            Icon(icon, contentDescription = trailingIconName)
          }
        }
      },
    )
  }

  private companion object {
    fun keyboardTypeOf(type: String): KeyboardType = when (type) {
      "email" -> KeyboardType.Email
      "number" -> KeyboardType.Number
      "phone" -> KeyboardType.Phone
      "decimal" -> KeyboardType.Decimal
      "url" -> KeyboardType.Uri
      else -> KeyboardType.Text
    }

    fun imeActionOf(type: String): ImeAction = when (type) {
      "go" -> ImeAction.Go
      "next" -> ImeAction.Next
      "search" -> ImeAction.Search
      "send" -> ImeAction.Send
      else -> ImeAction.Done
    }

    fun capitalizationOf(type: String): KeyboardCapitalization = when (type) {
      "none" -> KeyboardCapitalization.None
      "words" -> KeyboardCapitalization.Words
      "characters" -> KeyboardCapitalization.Characters
      else -> KeyboardCapitalization.Sentences
    }
  }
}
