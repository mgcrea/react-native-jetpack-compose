/* eslint-disable @typescript-eslint/consistent-type-definitions */
import type { DirectEventHandler, Double } from "react-native/Libraries/Types/CodegenTypes";
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";

/**
 * Event payload for the onConfirm event.
 */
export interface ConfirmEvent {
  selectedDateMillis: Double;
}

/**
 * Event payload for the onCancel event.
 */
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
  /** Controls the visibility of the date picker (for modal mode). */
  visible?: boolean | null;
  /** When true, renders embedded instead of as a modal dialog. */
  isInline?: boolean | null;

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

  /** Label for the confirm button (modal only). */
  confirmLabel?: string | null;
  /** Label for the cancel button (modal only). */
  cancelLabel?: string | null;
  /** Title text displayed at the top of the picker. */
  titleText?: string | null;

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
