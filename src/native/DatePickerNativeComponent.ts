import { codegenNativeComponent, type CodegenTypes, type HostComponent, type ViewProps } from "react-native";

/**
 * Event payload for the onConfirm event.
 */
export interface ConfirmEvent {
  selectedDateMillis: CodegenTypes.Double;
}

/**
 * Event payload for the onCancel event.
 */
export interface CancelEvent {}

/**
 * Event payload for the onDateChange event.
 */
export interface DateChangeEvent {
  selectedDateMillis: CodegenTypes.Double;
}

/**
 * Native props for the DatePicker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeDatePickerProps extends ViewProps {
  /** The selected date as epoch milliseconds. */
  selectedDateMillis?: CodegenTypes.WithDefault<CodegenTypes.Double, -1>;
  /** The initially displayed month as epoch milliseconds. */
  initialDisplayedMonthMillis?: CodegenTypes.WithDefault<CodegenTypes.Double, -1>;

  /** Minimum selectable date as epoch milliseconds. */
  minDateMillis?: CodegenTypes.WithDefault<CodegenTypes.Double, -1>;
  /** Maximum selectable date as epoch milliseconds. */
  maxDateMillis?: CodegenTypes.WithDefault<CodegenTypes.Double, -1>;
  /** Start year for the year selector. */
  yearRangeStart?: CodegenTypes.WithDefault<CodegenTypes.Double, -1>;
  /** End year for the year selector. */
  yearRangeEnd?: CodegenTypes.WithDefault<CodegenTypes.Double, -1>;

  /** Initial display mode: 'picker' or 'input'. */
  initialDisplayMode?: string | null;
  /** Whether to show the mode toggle button. */
  showModeToggle?: boolean | null;

  /** Label for the confirm button. */
  confirmLabel?: string | null;
  /** Label for the cancel button. */
  cancelLabel?: string | null;
  /** Title text displayed at the top of the picker. */
  titleText?: string | null;

  /** Floating label text for the text field. */
  label?: string | null;
  /** Placeholder text when no value selected. */
  placeholder?: string | null;
  /** Whether the picker is disabled. */
  disabled?: boolean | null;

  /** Native event handler fired when user confirms selection. */
  onConfirm?: CodegenTypes.DirectEventHandler<ConfirmEvent> | null;
  /** Native event handler fired when user cancels/dismisses. */
  onCancel?: CodegenTypes.DirectEventHandler<CancelEvent> | null;
  /** Native event handler fired when selection changes. */
  onDateChange?: CodegenTypes.DirectEventHandler<DateChangeEvent> | null;
}

/**
 * Native DatePicker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeDatePickerProps>(
  "DatePickerView",
) as HostComponent<NativeDatePickerProps>;
