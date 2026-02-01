package com.mgcrea.reactnative.jetpackcompose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.events.SegmentValueChangeEvent
import com.mgcrea.reactnative.jetpackcompose.events.SegmentValuesChangeEvent

@OptIn(ExperimentalMaterial3Api::class)
internal class SegmentedButtonView(reactContext: ThemedReactContext) :
  InlineComposeView(reactContext, TAG) {

  companion object {
    private const val TAG = "SegmentedButtonView"
  }

  // Data class for segment options
  data class SegmentOption(
    val value: String,
    val label: String,
    val icon: String? = null
  )

  // State backing for Compose
  private val _segments = mutableStateOf<List<SegmentOption>>(emptyList())
  private val _selectedValue = mutableStateOf<String?>(null)
  private val _selectedValues = mutableStateOf<Set<String>>(emptySet())
  private val _multiSelect = mutableStateOf(false)
  private val _disabled = mutableStateOf(false)

  // Property setters called by ViewManager
  fun setSegments(array: ReadableArray?) {
    _segments.value = array?.let { arr ->
      List(arr.size()) { i ->
        val map = arr.getMap(i)
        SegmentOption(
          value = map?.getString("value") ?: "",
          label = map?.getString("label") ?: "",
          icon = map?.getString("icon")
        )
      }
    } ?: emptyList()
  }

  fun setSelectedValue(value: String?) {
    _selectedValue.value = value
  }

  fun setSelectedValues(array: ReadableArray?) {
    _selectedValues.value = array?.let { arr ->
      (0 until arr.size()).mapNotNull { i ->
        arr.getString(i)
      }.toSet()
    } ?: emptySet()
  }

  fun setMultiSelect(value: Boolean) {
    _multiSelect.value = value
  }

  fun setDisabled(value: Boolean) {
    _disabled.value = value
  }

  // Icon mapping (extended from TextFieldView pattern)
  private fun getIcon(name: String?): ImageVector? = when (name?.lowercase()) {
    "check" -> Icons.Default.Check
    "star" -> Icons.Default.Star
    "favorite" -> Icons.Default.Favorite
    "home" -> Icons.Default.Home
    "settings" -> Icons.Default.Settings
    "person" -> Icons.Default.Person
    "email" -> Icons.Default.Email
    "phone" -> Icons.Default.Phone
    "search" -> Icons.Default.Search
    "add" -> Icons.Default.Add
    "close" -> Icons.Default.Close
    "done" -> Icons.Default.Done
    "edit" -> Icons.Default.Edit
    "delete" -> Icons.Default.Delete
    "share" -> Icons.Default.Share
    "info" -> Icons.Default.Info
    "warning" -> Icons.Default.Warning
    "notifications" -> Icons.Default.Notifications
    "list" -> Icons.AutoMirrored.Filled.List
    else -> null
  }

  @Composable
  override fun ComposeContent() {
    val segments = _segments.value
    val selectedValue = _selectedValue.value
    val selectedValues = _selectedValues.value
    val multiSelect = _multiSelect.value
    val disabled = _disabled.value

    if (segments.isEmpty()) return

    if (multiSelect) {
      MultiChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
      ) {
        segments.forEachIndexed { index, segment ->
          val isChecked = selectedValues.contains(segment.value)

          SegmentedButton(
            checked = isChecked,
            onCheckedChange = { checked ->
              if (!disabled) {
                val newValues = if (checked) {
                  selectedValues + segment.value
                } else {
                  selectedValues - segment.value
                }
                dispatchEvent(
                  SegmentValuesChangeEvent(
                    getSurfaceId(),
                    id,
                    newValues.toList()
                  )
                )
              }
            },
            enabled = !disabled,
            shape = SegmentedButtonDefaults.itemShape(
              index = index,
              count = segments.size
            ),
            icon = {
              SegmentedButtonDefaults.Icon(active = isChecked) {
                segment.icon?.let { iconName ->
                  getIcon(iconName)?.let { icon ->
                    Icon(
                      imageVector = icon,
                      contentDescription = null
                    )
                  }
                }
              }
            },
            label = { Text(segment.label) }
          )
        }
      }
    } else {
      SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
      ) {
        segments.forEachIndexed { index, segment ->
          val isSelected = segment.value == selectedValue

          SegmentedButton(
            selected = isSelected,
            onClick = {
              if (!disabled && segment.value != selectedValue) {
                dispatchEvent(
                  SegmentValueChangeEvent(
                    getSurfaceId(),
                    id,
                    segment.value,
                    index
                  )
                )
              }
            },
            enabled = !disabled,
            shape = SegmentedButtonDefaults.itemShape(
              index = index,
              count = segments.size
            ),
            icon = {
              SegmentedButtonDefaults.Icon(active = isSelected) {
                segment.icon?.let { iconName ->
                  getIcon(iconName)?.let { icon ->
                    Icon(
                      imageVector = icon,
                      contentDescription = null
                    )
                  }
                }
              }
            },
            label = { Text(segment.label) }
          )
        }
      }
    }
  }
}
