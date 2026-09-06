import { codegenNativeComponent, type CodegenTypes, type HostComponent, type ViewProps } from "react-native";

/**
 * Segment option type for the SegmentedButton.
 * Using Readonly types for codegen compatibility.
 */
export type NativeSegmentedButtonOption = Readonly<{
  value: string;
  label: string;
  icon?: string;
}>;

/**
 * Event payload for single-select mode (onValueChange).
 */
export type SegmentedButtonChangeEvent = Readonly<{
  value: string;
  index: CodegenTypes.Double;
}>;

/**
 * Event payload for multi-select mode (onValuesChange).
 */
export type SegmentedButtonMultiChangeEvent = Readonly<{
  values: string[];
}>;

/**
 * Native props for the SegmentedButton component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeSegmentedButtonProps extends ViewProps {
  /** Array of segment options */
  segments?: ReadonlyArray<NativeSegmentedButtonOption> | null;

  /** Currently selected value (single-select mode) */
  selectedValue?: string | null;

  /** Currently selected values (multi-select mode) */
  selectedValues?: ReadonlyArray<string> | null;

  /** Enable multi-select mode. Defaults to false (single-select). */
  multiSelect?: boolean | null;

  /** Whether the segmented button is disabled */
  disabled?: boolean | null;

  /** Native event handler for single-select mode */
  onValueChange?: CodegenTypes.DirectEventHandler<SegmentedButtonChangeEvent> | null;

  /** Native event handler for multi-select mode */
  onValuesChange?: CodegenTypes.DirectEventHandler<SegmentedButtonMultiChangeEvent> | null;
}

/**
 * Native SegmentedButton component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeSegmentedButtonProps>(
  "SegmentedButtonView",
) as HostComponent<NativeSegmentedButtonProps>;
