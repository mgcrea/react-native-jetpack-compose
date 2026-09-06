package com.mgcrea.reactnative.jetpackcompose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Resolves the icon names accepted by the TypeScript API to Material icons.
 *
 * Deliberately limited to `material-icons-core` (bundled with material3) so consumers
 * do not pay for the multi-megabyte `material-icons-extended` artifact.
 */
internal object MaterialIcons {
  private val icons: Map<String, ImageVector> by lazy {
    mapOf(
      "add" to Icons.Default.Add,
      "arrowdropdown" to Icons.Default.ArrowDropDown,
      "check" to Icons.Default.Check,
      "clear" to Icons.Default.Clear,
      "close" to Icons.Default.Close,
      "daterange" to Icons.Default.DateRange,
      "delete" to Icons.Default.Delete,
      "done" to Icons.Default.Done,
      "edit" to Icons.Default.Edit,
      "email" to Icons.Default.Email,
      "favorite" to Icons.Default.Favorite,
      "home" to Icons.Default.Home,
      "info" to Icons.Default.Info,
      "list" to Icons.AutoMirrored.Filled.List,
      "lock" to Icons.Default.Lock,
      "notifications" to Icons.Default.Notifications,
      "person" to Icons.Default.Person,
      "phone" to Icons.Default.Phone,
      "search" to Icons.Default.Search,
      "settings" to Icons.Default.Settings,
      "share" to Icons.Default.Share,
      "star" to Icons.Default.Star,
      "warning" to Icons.Default.Warning,
    )
  }

  /** Case-insensitive lookup; returns null for unknown names. */
  fun resolve(name: String?): ImageVector? = name?.let { icons[it.lowercase()] }
}
