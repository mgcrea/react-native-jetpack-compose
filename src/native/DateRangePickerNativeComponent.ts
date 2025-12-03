/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler, Double } from "react-native/Libraries/Types/CodegenTypes";

/**
 * Event payload for the onConfirm event.
 */
export interface RangeConfirmEvent {
  startDateMillis: Double;
  endDateMillis: Double;
}

/**
 * Event payload for the onCancel event.
 */
export interface RangeCancelEvent {}

/**
 * Event payload for the onDateChange event.
 */
export interface RangeDateChangeEvent {
  startDateMillis: Double;
  endDateMillis: Double;
}

/**
 * Native props for the DateRangePicker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeDateRangePickerProps extends ViewProps {
  /** The selected start date as epoch milliseconds. */
  startDateMillis?: Double | null;
  /** The selected end date as epoch milliseconds. */
  endDateMillis?: Double | null;
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
  onConfirm?: DirectEventHandler<RangeConfirmEvent> | null;
  /** Native event handler fired when user cancels/dismisses. */
  onCancel?: DirectEventHandler<RangeCancelEvent> | null;
  /** Native event handler fired when selection changes. */
  onDateChange?: DirectEventHandler<RangeDateChangeEvent> | null;
}

/**
 * Native DateRangePicker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeDateRangePickerProps>(
  "DateRangePickerView",
) as HostComponent<NativeDateRangePickerProps>;
