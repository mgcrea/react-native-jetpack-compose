/* eslint-disable @typescript-eslint/no-empty-object-type */
/* eslint-disable @typescript-eslint/array-type */
/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler, Double } from "react-native/Libraries/Types/CodegenTypes";

/**
 * Option type for the SheetPicker.
 * Using Readonly types for codegen compatibility.
 */
export type NativeSheetPickerOption = Readonly<{
  value: string;
  label: string;
}>;

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
  /** Array of options: [{ value: string, label: string }, ...] */
  options?: ReadonlyArray<NativeSheetPickerOption> | null;
  /** Currently selected value */
  selectedValue?: string | null;
  /** Title displayed at the top of the picker sheet */
  title?: string | null;
  /** Placeholder text for the search field */
  searchPlaceholder?: string | null;
  /** Whether to automatically dismiss the sheet after selection. Defaults to true. */
  autoDismiss?: boolean | null;
  /** Maximum height of the sheet as a ratio of screen height (0-1). Defaults to 0.9. */
  maxHeightRatio?: Double | null;
  /** Maximum width of the sheet in dp. When set, centers the sheet with this max width. */
  sheetMaxWidth?: Double | null;

  /** Floating label text for the text field. */
  label?: string | null;
  /** Placeholder text when no value selected. */
  placeholder?: string | null;
  /** Whether the picker is disabled. */
  disabled?: boolean | null;

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
