package com.mgcrea.reactnative.jetpackcompose.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier

/**
 * Read-only outlined text field used as the trigger of every picker.
 *
 * When [onPress] is provided, a release inside the field invokes it (the field is
 * read-only, so this is how the dialog or sheet is opened). The latest [onPress]
 * is always used, so callers can pass a closure over current state.
 */
@Composable
internal fun PickerTextField(
  value: String,
  label: String?,
  placeholder: String?,
  enabled: Boolean,
  trailingIcon: @Composable () -> Unit,
  modifier: Modifier = Modifier.fillMaxWidth(),
  colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
  onPress: (() -> Unit)? = null,
) {
  val interactionSource = remember { MutableInteractionSource() }
  if (onPress != null) {
    val currentOnPress by rememberUpdatedState(onPress)
    LaunchedEffect(interactionSource) {
      interactionSource.interactions.collect { interaction ->
        if (interaction is PressInteraction.Release) currentOnPress()
      }
    }
  }

  OutlinedTextField(
    modifier = modifier,
    value = value,
    onValueChange = {},
    readOnly = true,
    singleLine = true,
    enabled = enabled,
    label = label?.let { { Text(it) } },
    placeholder = placeholder?.let { { Text(it, maxLines = 1) } },
    trailingIcon = trailingIcon,
    colors = colors,
    interactionSource = interactionSource,
  )
}
