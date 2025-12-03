/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler } from "react-native/Libraries/Types/CodegenTypes";

/**
 * Event payload for the onItemSelect event.
 * Note: We use "onItemSelect" instead of "onSelect" to avoid conflict with
 * React Native's built-in "topSelect" bubbling event.
 */
export interface SheetPickerSelectEvent {
  value: string;
}

/**
 * Event payload for the onDismiss event.
 */
export interface SheetPickerDismissEvent {}

/**
 * Native props for the SheetPicker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeSheetPickerProps extends ViewProps {
  /** Controls the visibility of the sheet picker. */
  visible?: boolean | null;
  /** JSON string of options array: [{ value: string, label: string }, ...] */
  options?: string | null;
  /** Currently selected value */
  selectedValue?: string | null;
  /** Title displayed at the top of the picker sheet */
  title?: string | null;
  /** Placeholder text for the search field */
  searchPlaceholder?: string | null;
  /** Whether to automatically dismiss the sheet after selection. Defaults to true. */
  autoDismiss?: boolean | null;

  /** Native event handler fired when an option is selected. */
  onItemSelect?: DirectEventHandler<SheetPickerSelectEvent> | null;
  /** Native event handler fired when the sheet is dismissed. */
  onDismiss?: DirectEventHandler<SheetPickerDismissEvent> | null;
}

/**
 * Native SheetPicker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeSheetPickerProps>(
  "SheetPickerView",
) as HostComponent<NativeSheetPickerProps>;
