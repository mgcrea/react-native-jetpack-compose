package com.mgcrea.reactnative.jetpackcompose

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.events.ValueChangeEvent
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
internal class PickerView(reactContext: ThemedReactContext) :
  InlineComposeView(reactContext, TAG) {

  companion object {
    private const val TAG = "PickerView"
  }

  // State backing for Compose
  private val _items = mutableStateOf<List<PickerItem>>(emptyList())
  private val _selectedValue = mutableStateOf<String?>(null)
  private val _label = mutableStateOf<String?>(null)
  private val _placeholder = mutableStateOf<String?>(null)
  private val _disabled = mutableStateOf(false)
  private var _expanded by mutableStateOf(false)

  // Property setters called by ViewManager
  fun setItems(json: String?) {
    _items.value = json?.let { parseItems(it) } ?: emptyList()
  }

  fun setSelectedValue(value: String?) {
    _selectedValue.value = value
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

  private fun parseItems(json: String): List<PickerItem> {
    return try {
      val jsonArray = JSONArray(json)
      (0 until jsonArray.length()).map { i ->
        val obj = jsonArray.getJSONObject(i)
        PickerItem(
          value = obj.getString("value"),
          label = obj.getString("label")
        )
      }
    } catch (e: Exception) {
      Log.w(TAG, "Failed to parse items: $json", e)
      emptyList()
    }
  }

  @Composable
  override fun ComposeContent() {
    val items = _items.value
    val selectedValue = _selectedValue.value
    val label = _label.value
    val placeholder = _placeholder.value
    val disabled = _disabled.value

    val selectedItem = items.find { it.value == selectedValue }

    ExposedDropdownMenuBox(
      expanded = _expanded,
      onExpandedChange = { if (!disabled) _expanded = it }
    ) {
      OutlinedTextField(
        modifier = Modifier
          .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = !disabled)
          .fillMaxWidth(),
        value = selectedItem?.label ?: "",
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        enabled = !disabled,
        label = label?.let { { Text(it) } },
        placeholder = placeholder?.let { { Text(it, maxLines = 1) } },
        trailingIcon = {
          ExposedDropdownMenuDefaults.TrailingIcon(expanded = _expanded)
        },
        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
      )

      ExposedDropdownMenu(
        expanded = _expanded,
        onDismissRequest = { _expanded = false }
      ) {
        items.forEach { item ->
          DropdownMenuItem(
            text = { Text(item.label) },
            onClick = {
              _expanded = false
              if (item.value != selectedValue) {
                dispatchEvent(ValueChangeEvent(getSurfaceId(), id, item.value))
              }
            },
            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
          )
        }
      }
    }
  }

  data class PickerItem(val value: String, val label: String)
}
