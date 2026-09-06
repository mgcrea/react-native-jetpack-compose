package com.mgcrea.reactnative.jetpackcompose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Root theme for every Compose tree this package hosts.
 *
 * Without an explicit [MaterialTheme], `MaterialTheme.colorScheme` resolves to the
 * static light baseline, so dark mode renders unreadable. Follows the system setting.
 */
@Composable
internal fun ComposeTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
    content = content,
  )
}
