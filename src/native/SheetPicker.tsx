import React, { FunctionComponent, useCallback, useMemo } from "react";
import type { NativeSyntheticEvent, StyleProp, ViewStyle } from "react-native";

import SheetPickerNativeComponent, {
  type SheetPickerSelectEvent,
  type SheetPickerDismissEvent,
  type NativeSheetPickerProps,
} from "./SheetPickerNativeComponent";

/**
 * Represents a selectable option in the SheetPicker component.
 */
export interface SheetPickerOption {
  /** Unique value for this option (used in onChange callback) */
  value: string;
  /** Display label shown in the list */
  label: string;
}

/**
 * Props for the SheetPicker component.
 */
export type SheetPickerProps = Omit<
  NativeSheetPickerProps,
  "visible" | "options" | "selectedValue" | "autoDismiss" | "onItemSelect" | "onDismiss"
> & {
  /** Controls the visibility of the sheet picker. */
  visible?: boolean;
  /** Array of selectable options */
  options: SheetPickerOption[];
  /** Currently selected value */
  value?: string | null;
  /** Title displayed at the top of the picker sheet */
  title?: string;
  /** Placeholder text for the search field */
  searchPlaceholder?: string;
  /** Whether to automatically dismiss the sheet after selection. Defaults to true. */
  autoDismiss?: boolean;
  /** Callback fired when an option is selected */
  onSelect?: (value: string) => void;
  /** Callback fired when the sheet is dismissed */
  onDismiss?: () => void;
  /** Custom styles applied to the container */
  style?: StyleProp<ViewStyle>;
};

/**
 * A searchable picker component presented in a modal bottom sheet.
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
 *   visible={isOpen}
 *   options={countries}
 *   value={selectedCountry}
 *   title="Select Country"
 *   searchPlaceholder="Search countries..."
 *   onSelect={(value) => {
 *     setSelectedCountry(value);
 *     setIsOpen(false);
 *   }}
 *   onDismiss={() => setIsOpen(false)}
 * />
 * ```
 */
export const SheetPicker: FunctionComponent<SheetPickerProps> = ({
  visible = false,
  options,
  value,
  title,
  searchPlaceholder,
  autoDismiss = true,
  onSelect,
  onDismiss,
  style,
  ...props
}) => {
  // Serialize options to JSON for native side
  const optionsJson = useMemo(() => JSON.stringify(options), [options]);

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
      visible={visible}
      options={optionsJson}
      selectedValue={value}
      title={title}
      searchPlaceholder={searchPlaceholder}
      autoDismiss={autoDismiss}
      onItemSelect={handleSelect}
      onDismiss={handleDismiss}
      style={style}
    />
  );
};
