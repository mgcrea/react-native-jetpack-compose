package com.mgcrea.reactnative.jetpackcompose

import android.annotation.SuppressLint
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.compositionContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.facebook.react.uimanager.ThemedReactContext
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mgcrea.reactnative.jetpackcompose.core.InlineComposeView
import com.mgcrea.reactnative.jetpackcompose.events.DismissEvent
import com.mgcrea.reactnative.jetpackcompose.events.ItemSelectEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray

/**
 * Represents a selectable option in the SheetPicker.
 */
data class SheetPickerOption(val value: String, val label: String)

/**
 * SheetPicker implementation using Android's BottomSheetDialog with Compose UI.
 *
 * Features:
 * - Searchable list for filtering long option lists
 * - Checkmark indicator for selected item
 * - Auto-dismiss on selection (configurable)
 * - Material3 styling
 * - Read-only OutlinedTextField trigger
 */
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

  // Dialog state
  private var _showDialog by mutableStateOf(false)
  private var dialog: BottomSheetDialog? = null

  // Local search state (not exposed to React)
  private var _searchText by mutableStateOf("")

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

  private fun getSelectedLabel(): String {
    val selected = _selectedValue.value ?: return ""
    return _options.value.find { it.value == selected }?.label ?: ""
  }

  @Composable
  override fun ComposeContent() {
    val disabled = _disabled.value
    val label = _label.value
    val placeholder = _placeholder.value

    // Interaction source to detect clicks on the read-only text field
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
      interactionSource.interactions.collect { interaction ->
        if (interaction is PressInteraction.Release && !disabled) {
          showDialog()
        }
      }
    }

    // OutlinedTextField that shows selected value
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = getSelectedLabel(),
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
  }

  private fun showDialog() {
    if (_showDialog || dialog != null) return

    val activity = reactContext.currentActivity ?: return
    _showDialog = true

    dialog = BottomSheetDialog(activity).apply {
      setOnDismissListener {
        // Reset search when dismissed
        _searchText = ""
        if (_showDialog) {
          _showDialog = false
          dispatchEvent(DismissEvent(getSurfaceId(), id))
        }
        this@SheetPickerView.dialog = null
      }
      behavior.apply {
        isFitToContents = true
        skipCollapsed = true
        state = BottomSheetBehavior.STATE_EXPANDED
      }
      // Allow keyboard to show without resizing
      @Suppress("DEPRECATION")
      window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    val composeView = ComposeView(reactContext).apply {
      // Use the parent view's lifecycle owners
      setViewTreeLifecycleOwner(this@SheetPickerView)
      setViewTreeSavedStateRegistryOwner(this@SheetPickerView)

      val scope = CoroutineScope(AndroidUiDispatcher.CurrentThread)
      val recomposer = Recomposer(scope.coroutineContext)
      scope.launch { recomposer.runRecomposeAndApplyChanges() }
      compositionContext = recomposer

      setContent {
        MaterialTheme {
          SheetPickerContent()
        }
      }
    }

    dialog?.setContentView(composeView)
    dialog?.show()
    dialog?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
  }

  private fun handleSelect(value: String) {
    dispatchEvent(ItemSelectEvent(getSurfaceId(), id, value))
    if (_autoDismiss.value) {
      _searchText = ""
      _showDialog = false
      dialog?.dismiss()
    }
  }

  @Composable
  private fun DragHandle() {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .size(width = 32.dp, height = 4.dp)
          .clip(RoundedCornerShape(2.dp))
          .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
      )
    }
  }

  @Composable
  private fun SheetPickerContent() {
    val options = _options.value
    val selectedValue = _selectedValue.value
    val title = _title.value
    val searchPlaceholder = _searchPlaceholder.value
    val configuration = LocalConfiguration.current
    val maxHeight = (configuration.screenHeightDp * 0.85f).dp

    val filteredOptions by remember(options, _searchText) {
      derivedStateOf {
        val trimmed = _searchText.trim()
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

    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = maxHeight),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        // Drag handle
        DragHandle()

        // Content with padding
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
            value = _searchText,
            onValueChange = { _searchText = it },
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
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f, fill = false)
          ) {
            items(
              items = filteredOptions,
              key = { it.value }
            ) { option ->
              SheetPickerRow(
                option = option,
                isSelected = option.value == selectedValue,
                onSelect = { handleSelect(option.value) }
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
