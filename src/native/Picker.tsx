import React, { FunctionComponent, useCallback, useMemo } from "react";
import { StyleSheet, type NativeSyntheticEvent, type StyleProp, type ViewStyle } from "react-native";

import PickerNativeComponent, {
  type NativePickerProps,
  type PickerChangeEvent,
} from "./PickerNativeComponent";

/**
 * Represents a selectable item in the Picker component.
 */
export interface PickerItem {
  /** Unique value for this item (used in onChange callback) */
  value: string;
  /** Display label shown in the dropdown */
  label: string;
}

/**
 * Props for the Picker component.
 */
export type PickerProps = Omit<NativePickerProps, "items" | "selectedValue" | "disabled" | "onValueChange"> & {
  /** Array of selectable items */
  items: PickerItem[];
  /** Currently selected value */
  value?: string | null;
  /** Floating label text */
  label?: string;
  /** Placeholder text when no value selected */
  placeholder?: string;
  /** Whether the picker is disabled */
  disabled?: boolean;
  /** Callback fired when selection changes */
  onChange?: (value: string) => void;
  /** Custom styles applied to the container */
  style?: StyleProp<ViewStyle>;
};

/**
 * A dropdown picker component powered by Jetpack Compose's ExposedDropdownMenuBox.
 *
 * @example
 * ```tsx
 * const countries = [
 *   { value: "us", label: "United States" },
 *   { value: "uk", label: "United Kingdom" },
 * ];
 *
 * <Picker
 *   items={countries}
 *   value={country}
 *   label="Country"
 *   placeholder="Select a country"
 *   onChange={setCountry}
 * />
 * ```
 */
export const Picker: FunctionComponent<PickerProps> = ({
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
    (event: NativeSyntheticEvent<PickerChangeEvent>) => {
      onChange?.(event.nativeEvent.value);
    },
    [onChange],
  );

  return (
    <PickerNativeComponent
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
