import React, { useCallback, type FunctionComponent } from "react";
import { StyleSheet, type NativeSyntheticEvent, type StyleProp, type ViewStyle } from "react-native";

import SheetPickerNativeComponent, {
  type NativeSheetPickerProps,
  type SheetPickerDismissEvent,
  type SheetPickerSelectEvent,
} from "./SheetPickerNativeComponent";

/**
 * Represents a selectable option in the SheetPicker component.
 */
export type SheetPickerOption = {
  /** Unique value for this option (used in onChange callback) */
  value: string;
  /** Display label shown in the list */
  label: string;
};

/**
 * Props for the SheetPicker component.
 */
export type SheetPickerProps = Omit<
  NativeSheetPickerProps,
  | "options"
  | "selectedValue"
  | "autoDismiss"
  | "maxHeightRatio"
  | "sheetMaxWidth"
  | "disabled"
  | "onItemSelect"
  | "onDismiss"
> & {
  /** Array of selectable options */
  options: SheetPickerOption[];
  /** Currently selected value */
  value?: string | null;
  /** Title displayed at the top of the picker sheet */
  title?: string;
  /** Placeholder text for the search field in the sheet */
  searchPlaceholder?: string;
  /** Whether to automatically dismiss the sheet after selection. Defaults to true. */
  autoDismiss?: boolean;
  /** Maximum height of the sheet as a ratio of screen height (0-1). Defaults to 0.9. */
  maxHeightRatio?: number;
  /** Maximum width of the sheet in dp. When set, centers the sheet with this max width. */
  sheetMaxWidth?: number;
  /** Floating label text for the text field. */
  label?: string;
  /** Placeholder text when no value selected. */
  placeholder?: string;
  /** Whether the picker is disabled. */
  disabled?: boolean;
  /** Callback fired when an option is selected */
  onSelect?: (value: string) => void;
  /** Callback fired when the sheet is dismissed */
  onDismiss?: () => void;
  /** Custom styles applied to the container */
  style?: StyleProp<ViewStyle>;
};

/**
 * A searchable picker component with a text field that opens a modal bottom sheet.
 * Ideal for long lists like country or language selection.
 *
 * @example
 * ```tsx
 * const countries = [
 *   { value: "us", label: "United States" },
 *   { value: "uk", label: "United Kingdom" },
 *   { value: "fr", label: "France" },
 *   // ... more countries
 * ];
 *
 * <SheetPicker
 *   options={countries}
 *   value={selectedCountry}
 *   label="Country"
 *   placeholder="Select a country"
 *   title="Select Country"
 *   searchPlaceholder="Search countries..."
 *   onSelect={(value) => setSelectedCountry(value)}
 *   onDismiss={() => console.log('Sheet dismissed')}
 * />
 * ```
 */
export const SheetPicker: FunctionComponent<SheetPickerProps> = ({
  options,
  value,
  title,
  searchPlaceholder,
  autoDismiss = true,
  maxHeightRatio = 0.9,
  sheetMaxWidth,
  label,
  placeholder,
  disabled = false,
  onSelect,
  onDismiss,
  style,
  ...props
}) => {
  const handleSelect = useCallback(
    (event: NativeSyntheticEvent<SheetPickerSelectEvent>) => {
      onSelect?.(event.nativeEvent.value);
    },
    [onSelect],
  );

  const handleDismiss = useCallback(
    (_event: NativeSyntheticEvent<SheetPickerDismissEvent>) => {
      onDismiss?.();
    },
    [onDismiss],
  );

  return (
    <SheetPickerNativeComponent
      {...props}
      options={options}
      selectedValue={value}
      title={title}
      searchPlaceholder={searchPlaceholder}
      autoDismiss={autoDismiss}
      maxHeightRatio={maxHeightRatio}
      sheetMaxWidth={sheetMaxWidth}
      label={label}
      placeholder={placeholder}
      disabled={disabled}
      onItemSelect={handleSelect}
      onDismiss={handleDismiss}
      style={[styles.base, style]}
    />
  );
};

const styles = StyleSheet.create({
  base: {
    minHeight: 64,
  },
});
