package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.core.Option
import com.mgcrea.reactnative.jetpackcompose.core.emitEvent
import com.mgcrea.reactnative.jetpackcompose.events.EventNames
import com.mgcrea.reactnative.jetpackcompose.ui.PickerTextField

/**
 * Dropdown picker backed by Material3's [ExposedDropdownMenuBox].
 */
@OptIn(ExperimentalMaterial3Api::class)
internal class PickerView(reactContext: ThemedReactContext) : InlineComposeView(reactContext) {

  var items: List<Option> by mutableStateOf(emptyList())
  var selectedValue: String? by mutableStateOf(null)
  var label: String? by mutableStateOf(null)
  var placeholder: String? by mutableStateOf(null)
  var disabled: Boolean by mutableStateOf(false)

  private var expanded by mutableStateOf(false)

  override fun onDropInstance() {
    expanded = false
    super.onDropInstance()
  }

  @Composable
  override fun ComposeContent() {
    val items = items
    val selectedValue = selectedValue
    val selectedLabel = remember(items, selectedValue) {
      items.firstOrNull { it.value == selectedValue }?.label ?: ""
    }

    ExposedDropdownMenuBox(
      expanded = expanded,
      onExpandedChange = { if (!disabled) expanded = it },
    ) {
      PickerTextField(
        value = selectedLabel,
        label = label,
        placeholder = placeholder,
        enabled = !disabled,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        modifier = Modifier
          .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = !disabled)
          .fillMaxWidth(),
        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
      )

      ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
      ) {
        items.forEachIndexed { index, item ->
          DropdownMenuItem(
            text = { Text(item.label) },
            onClick = {
              expanded = false
              if (item.value != selectedValue) {
                emitEvent(EventNames.VALUE_CHANGE) {
                  putString("value", item.value)
                  putDouble("index", index.toDouble())
                }
              }
            },
            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
          )
        }
      }
    }
  }
}
