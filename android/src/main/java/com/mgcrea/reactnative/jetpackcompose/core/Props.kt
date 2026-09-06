package com.mgcrea.reactnative.jetpackcompose.core

import com.facebook.react.bridge.ReadableArray

/**
 * Sentinel that codegen substitutes for an unset numeric prop
 * (declared as `WithDefault<Double | Int32, -1>` in the TypeScript specs).
 */
internal const val UNSET_NUMBER = -1

/** Maps the codegen sentinel back to `null`. */
internal fun Double.orNullIfUnset(): Double? = takeUnless { it == UNSET_NUMBER.toDouble() }

/** Maps the codegen sentinel back to `null`. */
internal fun Int.orNullIfUnset(): Int? = takeUnless { it == UNSET_NUMBER }

/**
 * A selectable option shared by the Picker, SheetPicker and SegmentedButton views.
 */
internal data class Option(val value: String, val label: String, val icon: String? = null)

/**
 * Parses an array of `{ value, label, icon? }` maps. A missing label falls back to the value.
 */
internal fun ReadableArray?.toOptions(): List<Option> {
  if (this == null) return emptyList()
  return List(size()) { i ->
    val map = getMap(i)
    val value = map?.getString("value") ?: ""
    Option(
      value = value,
      label = map?.getString("label") ?: value,
      icon = map?.getString("icon"),
    )
  }
}

/** Parses an array of strings into a set, skipping null entries. */
internal fun ReadableArray?.toStringSet(): Set<String> {
  if (this == null) return emptySet()
  return buildSet(size()) {
    for (i in 0 until size()) getString(i)?.let(::add)
  }
}
