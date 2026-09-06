import type { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";

import { useNativeEvent } from "../hooks/useNativeEvent";
import { fieldStyles, useFieldStyle } from "../utils/style";
import PickerNativeComponent, {
  type NativePickerProps,
  type PickerChangeEvent,
} from "./PickerNativeComponent";

/**
 * Represents a selectable option in the Picker component.
 */
export type PickerOption = {
  /** Unique value for this item (used in onChange callback) */
  value: string;
  /** Display label shown in the dropdown */
  label: string;
};

/**
 * Props for the Picker component.
 */
export type PickerProps = Omit<NativePickerProps, "items" | "selectedValue" | "disabled" | "onValueChange"> & {
  /** Array of selectable options */
  options: PickerOption[];
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

const toValue = (event: PickerChangeEvent) => event.value;

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
 *   options={countries}
 *   value={country}
 *   label="Country"
 *   placeholder="Select a country"
 *   onChange={setCountry}
 * />
 * ```
 */
export const Picker: FunctionComponent<PickerProps> = ({
  options,
  value,
  label,
  placeholder,
  disabled = false,
  onChange,
  style,
  ...props
}) => {
  const handleValueChange = useNativeEvent(onChange, toValue);

  return (
    <PickerNativeComponent
      {...props}
      items={options}
      selectedValue={value}
      label={label}
      placeholder={placeholder}
      disabled={disabled}
      onValueChange={handleValueChange}
      style={useFieldStyle(fieldStyles.field, style)}
    />
  );
};
