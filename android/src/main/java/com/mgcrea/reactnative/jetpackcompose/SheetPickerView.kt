package com.mgcrea.reactnative.jetpackcompose

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.facebook.react.uimanager.ThemedReactContext
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.core.Option
import com.mgcrea.reactnative.jetpackcompose.core.emitEvent
import com.mgcrea.reactnative.jetpackcompose.events.EventNames
import com.mgcrea.reactnative.jetpackcompose.ui.PickerTextField
import kotlinx.coroutines.launch

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
internal class SheetPickerView(reactContext: ThemedReactContext) : InlineComposeView(reactContext) {

  var options: List<Option> by mutableStateOf(emptyList())
  var selectedValue: String? by mutableStateOf(null)
  var title: String? by mutableStateOf(null)
  var searchPlaceholder: String by mutableStateOf("Search")
  var autoDismiss: Boolean by mutableStateOf(true)
  var maxHeightRatio: Float by mutableStateOf(0.9f)
  var sheetMaxWidth: Float? by mutableStateOf(null)
  var label: String? by mutableStateOf(null)
  var placeholder: String? by mutableStateOf(null)
  var disabled: Boolean by mutableStateOf(false)

  private var showSheet by mutableStateOf(false)

  override fun onDropInstance() {
    showSheet = false
    super.onDropInstance()
  }

  @Composable
  override fun ComposeContent() {
    val options = options
    val selectedValue = selectedValue
    val disabled = disabled

    val selectedLabel = remember(selectedValue, options) {
      options.firstOrNull { it.value == selectedValue }?.label ?: ""
    }

    PickerTextField(
      value = selectedLabel,
      label = label,
      placeholder = placeholder,
      enabled = !disabled,
      trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Open picker") },
      onPress = { if (!disabled) showSheet = true },
    )

    // ModalBottomSheet - part of the same Compose tree
    if (showSheet) {
      val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
      val scope = rememberCoroutineScope()
      var searchText by remember { mutableStateOf("") }

      // Calculate max height based on screen height and ratio
      val maxSheetHeight = (LocalConfiguration.current.screenHeightDp * maxHeightRatio).dp
      val sheetMaxWidthDp = sheetMaxWidth?.dp ?: Dp.Unspecified

      val filteredOptions = remember(options, searchText) {
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

      fun dismiss() {
        showSheet = false
        emitEvent(EventNames.DISMISS)
      }

      ModalBottomSheet(
        onDismissRequest = ::dismiss,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidthDp,
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxSheetHeight)
            .padding(horizontal = 16.dp)
            .navigationBarsPadding(),
        ) {
          title?.let {
            Text(
              text = it,
              style = MaterialTheme.typography.titleLarge,
              modifier = Modifier.padding(bottom = 16.dp),
            )
          }

          OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(searchPlaceholder) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            },
            singleLine = true,
          )

          Spacer(modifier = Modifier.height(8.dp))

          LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(items = filteredOptions, key = { it.value }) { option ->
              SheetPickerRow(
                option = option,
                isSelected = option.value == selectedValue,
                onSelect = {
                  emitEvent(EventNames.ITEM_SELECT) { putString("value", option.value) }
                  if (autoDismiss) {
                    scope.launch {
                      sheetState.hide()
                      showSheet = false
                    }
                  }
                },
              )
            }
          }
        }
      }
    }
  }

  @Composable
  private fun SheetPickerRow(
    option: Option,
    isSelected: Boolean,
    onSelect: () -> Unit,
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onSelect)
        .padding(vertical = 12.dp, horizontal = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = option.label,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f),
      )

      Spacer(modifier = Modifier.width(12.dp))

      if (isSelected) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Selected",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(24.dp),
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
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
          ),
        ) {}
      }
    }
  }
}
