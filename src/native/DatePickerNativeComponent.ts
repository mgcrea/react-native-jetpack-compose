/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler, Double } from "react-native/Libraries/Types/CodegenTypes";

/**
 * Event payload for the onConfirm event.
 */
export interface ConfirmEvent {
  selectedDateMillis: Double;
}

/**
 * Event payload for the onCancel event.
 */
// eslint-disable-next-line @typescript-eslint/no-empty-object-type
export interface CancelEvent {}

/**
 * Event payload for the onDateChange event.
 */
export interface DateChangeEvent {
  selectedDateMillis: Double;
}

/**
 * Native props for the DatePicker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeDatePickerProps extends ViewProps {
  /** The selected date as epoch milliseconds. */
  selectedDateMillis?: Double | null;
  /** The initially displayed month as epoch milliseconds. */
  initialDisplayedMonthMillis?: Double | null;

  /** Minimum selectable date as epoch milliseconds. */
  minDateMillis?: Double | null;
  /** Maximum selectable date as epoch milliseconds. */
  maxDateMillis?: Double | null;
  /** Start year for the year selector. */
  yearRangeStart?: Double | null;
  /** End year for the year selector. */
  yearRangeEnd?: Double | null;

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
  onConfirm?: DirectEventHandler<ConfirmEvent> | null;
  /** Native event handler fired when user cancels/dismisses. */
  onCancel?: DirectEventHandler<CancelEvent> | null;
  /** Native event handler fired when selection changes. */
  onDateChange?: DirectEventHandler<DateChangeEvent> | null;
}

/**
 * Native DatePicker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeDatePickerProps>(
  "DatePickerView",
) as HostComponent<NativeDatePickerProps>;
