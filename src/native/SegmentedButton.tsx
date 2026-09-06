import type { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";

import { useNativeEvent } from "../hooks/useNativeEvent";
import { fieldStyles, useFieldStyle } from "../utils/style";
import SegmentedButtonNativeComponent, {
  type NativeSegmentedButtonProps,
  type SegmentedButtonChangeEvent,
  type SegmentedButtonMultiChangeEvent,
} from "./SegmentedButtonNativeComponent";

/**
 * Represents a segment option in the SegmentedButton component.
 */
export type SegmentedButtonOption = {
  /** Unique value for this segment */
  value: string;
  /** Display label for the segment */
  label: string;
  /** Optional Material Icon name (e.g., "Check", "Star", "Favorite") */
  icon?: string;
};

/**
 * Base props shared by both single and multi-select modes.
 */
type BaseSegmentedButtonProps = Omit<
  NativeSegmentedButtonProps,
  | "segments"
  | "selectedValue"
  | "selectedValues"
  | "multiSelect"
  | "disabled"
  | "onValueChange"
  | "onValuesChange"
> & {
  /** Array of segment options */
  options: SegmentedButtonOption[];
  /** Whether the segmented button is disabled */
  disabled?: boolean;
  /** Custom styles applied to the container */
  style?: StyleProp<ViewStyle>;
};

/**
 * Props for single-select mode (default).
 */
export type SingleSelectSegmentedButtonProps = BaseSegmentedButtonProps & {
  /** Multi-select mode disabled (default) */
  multiSelect?: false;
  /** Currently selected value */
  value?: string | null;
  /** Callback fired when selection changes */
  onChange?: (value: string) => void;
};

/**
 * Props for multi-select mode.
 */
export type MultiSelectSegmentedButtonProps = BaseSegmentedButtonProps & {
  /** Enable multi-select mode */
  multiSelect: true;
  /** Currently selected values */
  value?: string[];
  /** Callback fired when selection changes */
  onChange?: (values: string[]) => void;
};

/**
 * Combined props type using discriminated union.
 */
export type SegmentedButtonProps = SingleSelectSegmentedButtonProps | MultiSelectSegmentedButtonProps;

const toValue = (event: SegmentedButtonChangeEvent) => event.value;
const toValues = (event: SegmentedButtonMultiChangeEvent) => [...event.values];

/**
 * A segmented button component powered by Jetpack Compose's SegmentedButtonRow.
 *
 * Supports both single-select (default) and multi-select modes.
 *
 * @example Single-select mode (default)
 * ```tsx
 * const sizes = [
 *   { value: "s", label: "S" },
 *   { value: "m", label: "M" },
 *   { value: "l", label: "L" },
 * ];
 *
 * <SegmentedButton
 *   options={sizes}
 *   value={size}
 *   onChange={setSize}
 * />
 * ```
 *
 * @example Multi-select mode with icons
 * ```tsx
 * const formats = [
 *   { value: "bold", label: "B", icon: "FormatBold" },
 *   { value: "italic", label: "I", icon: "FormatItalic" },
 *   { value: "underline", label: "U", icon: "FormatUnderlined" },
 * ];
 *
 * <SegmentedButton
 *   multiSelect
 *   options={formats}
 *   value={activeFormats}
 *   onChange={setActiveFormats}
 * />
 * ```
 */
export const SegmentedButton: FunctionComponent<SegmentedButtonProps> = (props) => {
  const { options, disabled = false, style, ...restProps } = props;
  // Narrow the discriminated union once so the handlers below need no casts
  const single = props.multiSelect ? undefined : props;
  const multi = props.multiSelect ? props : undefined;

  const handleSingleChange = useNativeEvent(single?.onChange, toValue);
  const handleMultiChange = useNativeEvent(multi?.onChange, toValues);

  return (
    <SegmentedButtonNativeComponent
      {...omitOwnProps(restProps)}
      segments={options}
      selectedValue={single?.value}
      selectedValues={multi?.value}
      multiSelect={props.multiSelect ?? false}
      disabled={disabled}
      onValueChange={handleSingleChange}
      onValuesChange={handleMultiChange}
      style={useFieldStyle(fieldStyles.segmented, style)}
    />
  );
};

/** Strips the wrapper-only props before spreading the remainder onto the native component. */
function omitOwnProps<T extends { value?: unknown; multiSelect?: unknown; onChange?: unknown }>({
  value: _value,
  multiSelect: _multiSelect,
  onChange: _onChange,
  ...rest
}: T) {
  return rest;
}
