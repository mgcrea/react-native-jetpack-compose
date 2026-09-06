import { codegenNativeComponent, type CodegenTypes, type HostComponent, type ViewProps } from "react-native";

/**
 * Option type for the Picker.
 * Using Readonly types for codegen compatibility.
 */
export type NativePickerOption = Readonly<{
  value: string;
  label: string;
}>;

/**
 * Event payload for the onValueChange event.
 */
export type PickerChangeEvent = Readonly<{
  value: string;
  index: CodegenTypes.Double;
}>;

/**
 * Native props for the Picker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativePickerProps extends ViewProps {
  /** Array of options: [{ value: string, label: string }, ...] */
  items?: ReadonlyArray<NativePickerOption> | null;
  /** Currently selected value */
  selectedValue?: string | null;
  /** Floating label text */
  label?: string | null;
  /** Placeholder text when no value selected */
  placeholder?: string | null;
  /** Whether the picker is disabled */
  disabled?: boolean | null;

  /** Native event handler fired when selection changes. */
  onValueChange?: CodegenTypes.DirectEventHandler<PickerChangeEvent> | null;
}

/**
 * Native Picker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativePickerProps>("PickerView") as HostComponent<NativePickerProps>;
