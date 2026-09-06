import { codegenNativeComponent, type CodegenTypes, type HostComponent, type ViewProps } from "react-native";

/**
 * Event payload for the onConfirm event.
 */
export interface RangeConfirmEvent {
  startDateMillis: CodegenTypes.Double;
  endDateMillis: CodegenTypes.Double;
}

/**
 * Event payload for the onCancel event.
 */
export interface RangeCancelEvent {}

/**
 * Event payload for the onDateChange event.
 */
export interface RangeDateChangeEvent {
  /** Null while the start date has not been picked yet. */
  startDateMillis?: CodegenTypes.Double | null;
  /** Null while the end date has not been picked yet. */
  endDateMillis?: CodegenTypes.Double | null;
}

/**
 * Native props for the DateRangePicker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeDateRangePickerProps extends ViewProps {
  /** The selected start date as epoch milliseconds. */
  startDateMillis?: CodegenTypes.WithDefault<CodegenTypes.Double, -1>;
  /** The selected end date as epoch milliseconds. */
  endDateMillis?: CodegenTypes.WithDefault<CodegenTypes.Double, -1>;
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
  onConfirm?: CodegenTypes.DirectEventHandler<RangeConfirmEvent> | null;
  /** Native event handler fired when user cancels/dismisses. */
  onCancel?: CodegenTypes.DirectEventHandler<RangeCancelEvent> | null;
  /** Native event handler fired when selection changes. */
  onDateChange?: CodegenTypes.DirectEventHandler<RangeDateChangeEvent> | null;
}

/**
 * Native DateRangePicker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeDateRangePickerProps>(
  "DateRangePickerView",
) as HostComponent<NativeDateRangePickerProps>;
