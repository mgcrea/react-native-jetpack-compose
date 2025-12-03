/* eslint-disable @typescript-eslint/consistent-type-definitions */
import type { DirectEventHandler } from "react-native/Libraries/Types/CodegenTypes";
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";

/**
 * Event payload for the onValueChange event.
 */
export interface PickerChangeEvent {
  value: string;
}

/**
 * Native props for the Picker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativePickerProps extends ViewProps {
  /** JSON string of items array: [{ value: string, label: string }, ...] */
  items?: string | null;
  /** Currently selected value */
  selectedValue?: string | null;
  /** Floating label text */
  label?: string | null;
  /** Placeholder text when no value selected */
  placeholder?: string | null;
  /** Whether the picker is disabled */
  disabled?: boolean | null;

  /** Native event handler fired when selection changes. */
  onValueChange?: DirectEventHandler<PickerChangeEvent> | null;
}

/**
 * Native Picker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativePickerProps>(
  "PickerView",
) as HostComponent<NativePickerProps>;
