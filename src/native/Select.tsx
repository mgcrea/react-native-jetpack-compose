import React, { FunctionComponent, useCallback, useMemo } from "react";
import type { NativeSyntheticEvent, StyleProp, ViewStyle } from "react-native";
import { StyleSheet } from "react-native";

import SelectNativeComponent, {
  type SelectChangeEvent,
  type NativeSelectProps,
} from "./SelectNativeComponent";

/**
 * Represents a selectable item in the Select component.
 */
export interface SelectItem {
  /** Unique value for this item (used in onChange callback) */
  value: string;
  /** Display label shown in the dropdown */
  label: string;
}

/**
 * Props for the Select component.
 */
export type SelectProps = Omit<
  NativeSelectProps,
  "items" | "selectedValue" | "disabled" | "onValueChange"
> & {
  /** Array of selectable items */
  items: SelectItem[];
  /** Currently selected value */
  value?: string | null;
  /** Floating label text */
  label?: string;
  /** Placeholder text when no value selected */
  placeholder?: string;
  /** Whether the select is disabled */
  disabled?: boolean;
  /** Callback fired when selection changes */
  onChange?: (value: string) => void;
  /** Custom styles applied to the container */
  style?: StyleProp<ViewStyle>;
};

/**
 * A dropdown select component powered by Jetpack Compose's ExposedDropdownMenuBox.
 *
 * @example
 * ```tsx
 * const countries = [
 *   { value: "us", label: "United States" },
 *   { value: "uk", label: "United Kingdom" },
 * ];
 *
 * <Select
 *   items={countries}
 *   value={country}
 *   label="Country"
 *   placeholder="Select a country"
 *   onChange={setCountry}
 * />
 * ```
 */
export const Select: FunctionComponent<SelectProps> = ({
  items,
  value,
  label,
  placeholder,
  disabled = false,
  onChange,
  style,
  ...props
}) => {
  // Serialize items to JSON for native side
  const itemsJson = useMemo(() => JSON.stringify(items), [items]);

  const handleValueChange = useCallback(
    (event: NativeSyntheticEvent<SelectChangeEvent>) => {
      onChange?.(event.nativeEvent.value);
    },
    [onChange],
  );

  return (
    <SelectNativeComponent
      {...props}
      items={itemsJson}
      selectedValue={value}
      label={label}
      placeholder={placeholder}
      disabled={disabled}
      onValueChange={handleValueChange}
      style={[styles.base, style]}
    />
  );
};

const styles = StyleSheet.create({
  base: {
    minHeight: 64,
  },
});
