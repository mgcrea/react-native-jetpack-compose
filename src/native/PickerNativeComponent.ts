/* eslint-disable @typescript-eslint/array-type */
/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler, Double } from "react-native/Libraries/Types/CodegenTypes";

/**
 * Item type for the Picker.
 * Using Readonly types for codegen compatibility.
 */
export type NativePickerItem = Readonly<{
  value: string;
  label: string;
}>;

/**
 * Event payload for the onValueChange event.
 */
export type PickerChangeEvent = Readonly<{
  value: string;
  index: Double;
}>;

/**
 * Native props for the Picker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativePickerProps extends ViewProps {
  /** Array of items: [{ value: string, label: string }, ...] */
  items?: ReadonlyArray<NativePickerItem> | null;
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
export default codegenNativeComponent<NativePickerProps>("PickerView") as HostComponent<NativePickerProps>;
