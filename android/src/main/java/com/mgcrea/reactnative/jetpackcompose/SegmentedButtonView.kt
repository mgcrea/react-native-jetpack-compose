package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.facebook.react.bridge.Arguments
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.core.Option
import com.mgcrea.reactnative.jetpackcompose.core.emitEvent
import com.mgcrea.reactnative.jetpackcompose.events.EventNames
import com.mgcrea.reactnative.jetpackcompose.ui.MaterialIcons

/**
 * Single- or multi-choice Material3 segmented button row.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal class SegmentedButtonView(reactContext: ThemedReactContext) : InlineComposeView(reactContext) {

  var segments: List<Option> by mutableStateOf(emptyList())
  var selectedValue: String? by mutableStateOf(null)
  var selectedValues: Set<String> by mutableStateOf(emptySet())
  var multiSelect: Boolean by mutableStateOf(false)
  var disabled: Boolean by mutableStateOf(false)

  @Composable
  override fun ComposeContent() {
    val segments = segments
    val disabled = disabled
    if (segments.isEmpty()) return

    if (multiSelect) {
      val selectedValues = selectedValues
      MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        segments.forEachIndexed { index, segment ->
          key(segment.value) {
            val isChecked = segment.value in selectedValues
            SegmentedButton(
              checked = isChecked,
              onCheckedChange = { checked ->
                if (!disabled) {
                  val newValues = if (checked) selectedValues + segment.value else selectedValues - segment.value
                  emitEvent(EventNames.VALUES_CHANGE) {
                    putArray("values", Arguments.fromList(newValues.toList()))
                  }
                }
              },
              enabled = !disabled,
              shape = SegmentedButtonDefaults.itemShape(index = index, count = segments.size),
              icon = { SegmentIcon(segment, active = isChecked) },
              label = { Text(segment.label) },
            )
          }
        }
      }
    } else {
      val selectedValue = selectedValue
      SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        segments.forEachIndexed { index, segment ->
          key(segment.value) {
            val isSelected = segment.value == selectedValue
            SegmentedButton(
              selected = isSelected,
              onClick = {
                if (!disabled && segment.value != selectedValue) {
                  emitEvent(EventNames.VALUE_CHANGE) {
                    putString("value", segment.value)
                    putDouble("index", index.toDouble())
                  }
                }
              },
              enabled = !disabled,
              shape = SegmentedButtonDefaults.itemShape(index = index, count = segments.size),
              icon = { SegmentIcon(segment, active = isSelected) },
              label = { Text(segment.label) },
            )
          }
        }
      }
    }
  }

  @Composable
  private fun SegmentIcon(segment: Option, active: Boolean) {
    SegmentedButtonDefaults.Icon(active = active) {
      MaterialIcons.resolve(segment.icon)?.let { icon ->
        Icon(imageVector = icon, contentDescription = null)
      }
    }
  }
}
