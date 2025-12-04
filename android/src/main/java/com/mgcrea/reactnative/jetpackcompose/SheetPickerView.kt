package com.mgcrea.reactnative.jetpackcompose

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.events.DismissEvent
import com.mgcrea.reactnative.jetpackcompose.events.ItemSelectEvent
import kotlinx.coroutines.launch
import org.json.JSONArray

/**
 * Represents a selectable option in the SheetPicker.
 */
data class SheetPickerOption(val value: String, val label: String)

/**
 * SheetPicker implementation using Compose's ModalBottomSheet.
 *
 * Uses a single Compose tree (no separate Recomposer) to avoid heap corruption
 * issues when the view is destroyed.
 *
 * Features:
 * - Searchable list for filtering long option lists
 * - Checkmark indicator for selected item
 * - Auto-dismiss on selection (configurable)
 * - Material3 styling
 * - Read-only OutlinedTextField trigger
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewConstructor")
internal class SheetPickerView(reactContext: ThemedReactContext) :
  InlineComposeView(reactContext, TAG) {

  companion object {
    private const val TAG = "SheetPickerView"
  }

  // State backing for Compose
  private val _options = mutableStateOf<List<SheetPickerOption>>(emptyList())
  private val _selectedValue = mutableStateOf<String?>(null)
  private val _title = mutableStateOf<String?>(null)
  private val _searchPlaceholder = mutableStateOf("Search")
  private val _autoDismiss = mutableStateOf(true)

  // New state for OutlinedTextField
  private val _label = mutableStateOf<String?>(null)
  private val _placeholder = mutableStateOf<String?>(null)
  private val _disabled = mutableStateOf(false)

  // Dialog state - all in Compose now
  private var _showSheet by mutableStateOf(false)

  // Property setters called by ViewManager
  fun setOptions(json: String?) {
    _options.value = json?.let { parseOptions(it) } ?: emptyList()
  }

  fun setSelectedValue(value: String?) {
    _selectedValue.value = value
  }

  fun setTitle(value: String?) {
    _title.value = value
  }

  fun setSearchPlaceholder(value: String?) {
    _searchPlaceholder.value = value ?: "Search"
  }

  fun setAutoDismiss(value: Boolean) {
    _autoDismiss.value = value
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

  override fun onDropInstance() {
    _showSheet = false
    super.onDropInstance()
  }

  private fun parseOptions(json: String): List<SheetPickerOption> {
    return try {
      val jsonArray = JSONArray(json)
      (0 until jsonArray.length()).map { i ->
        val obj = jsonArray.getJSONObject(i)
        SheetPickerOption(
          value = obj.getString("value"),
          label = obj.optString("label", obj.getString("value"))
        )
      }
    } catch (e: Exception) {
      Log.w(TAG, "Failed to parse options: $json", e)
      emptyList()
    }
  }

  @Composable
  override fun ComposeContent() {
    val options = _options.value
    val selectedValue = _selectedValue.value
    val disabled = _disabled.value
    val label = _label.value
    val placeholder = _placeholder.value
    val title = _title.value
    val searchPlaceholder = _searchPlaceholder.value
    val autoDismiss = _autoDismiss.value

    // Local search state
    var searchText by remember { mutableStateOf("") }

    // Get the selected label
    val selectedLabel = remember(selectedValue, options) {
      options.find { it.value == selectedValue }?.label ?: ""
    }

    // Interaction source to detect clicks on the read-only text field
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
      interactionSource.interactions.collect { interaction ->
        if (interaction is PressInteraction.Release && !disabled) {
          _showSheet = true
        }
      }
    }

    // OutlinedTextField that shows selected value
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = selectedLabel,
      onValueChange = {},
      readOnly = true,
      singleLine = true,
      enabled = !disabled,
      label = label?.let { { Text(it) } },
      placeholder = placeholder?.let { { Text(it, maxLines = 1) } },
      trailingIcon = {
        Icon(Icons.Default.ArrowDropDown, contentDescription = "Open picker")
      },
      interactionSource = interactionSource
    )

    // ModalBottomSheet - part of the same Compose tree
    if (_showSheet) {
      val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
      val scope = rememberCoroutineScope()

      // Filter options based on search
      val filteredOptions by remember(options, searchText) {
        derivedStateOf {
          val trimmed = searchText.trim()
          if (trimmed.isEmpty()) {
            options
          } else {
            options.filter { option ->
              option.label.contains(trimmed, ignoreCase = true) ||
                option.value.contains(trimmed, ignoreCase = true)
            }
          }
        }
      }

      ModalBottomSheet(
        onDismissRequest = {
          searchText = ""
          _showSheet = false
          dispatchEvent(DismissEvent(getSurfaceId(), id))
        },
        sheetState = sheetState
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
        ) {
          // Title
          title?.let {
            Text(
              text = it,
              style = MaterialTheme.typography.titleLarge,
              modifier = Modifier.padding(bottom = 16.dp)
            )
          }

          // Search field
          OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(searchPlaceholder) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            },
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Options list
          LazyColumn(
            modifier = Modifier.fillMaxWidth()
          ) {
            items(
              items = filteredOptions,
              key = { it.value }
            ) { option ->
              SheetPickerRow(
                option = option,
                isSelected = option.value == selectedValue,
                onSelect = {
                  dispatchEvent(ItemSelectEvent(getSurfaceId(), id, option.value))
                  if (autoDismiss) {
                    scope.launch {
                      sheetState.hide()
                      searchText = ""
                      _showSheet = false
                    }
                  }
                }
              )
            }
          }
        }
      }
    }
  }

  @Composable
  private fun SheetPickerRow(
    option: SheetPickerOption,
    isSelected: Boolean,
    onSelect: () -> Unit
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onSelect)
        .padding(vertical = 12.dp, horizontal = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = option.label,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f)
      )

      Spacer(modifier = Modifier.width(12.dp))

      if (isSelected) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Selected",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(24.dp)
        )
      } else {
        // Empty circle outline
        Surface(
          modifier = Modifier
            .size(20.dp)
            .clip(CircleShape),
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface,
          border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
          )
        ) {}
      }
    }
  }
}
