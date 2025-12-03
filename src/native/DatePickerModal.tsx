import React, { FunctionComponent, useCallback, useMemo } from "react";
import type { NativeSyntheticEvent, StyleProp, ViewStyle } from "react-native";
import { StyleSheet } from "react-native";

import DatePickerNativeComponent, {
  type CancelEvent,
  type ConfirmEvent,
  type DateChangeEvent,
  type NativeDatePickerProps,
} from "./DatePickerNativeComponent";

/**
 * Converts a Date, number (epoch millis), or ISO string to epoch milliseconds.
 */
function toEpochMillis(date: Date | number | string | undefined | null): number | undefined {
  if (date == null) return undefined;
  if (typeof date === "number") return date;
  if (typeof date === "string") return new Date(date).getTime();
  return date.getTime();
}

/**
 * Year range configuration.
 */
export interface YearRange {
  start: number;
  end: number;
}

/**
 * Props for the DatePickerModal component (modal/dialog mode).
 */
export type DatePickerModalProps = Omit<
  NativeDatePickerProps,
  | "isInline"
  | "selectedDateMillis"
  | "initialDisplayedMonthMillis"
  | "minDateMillis"
  | "maxDateMillis"
  | "yearRangeStart"
  | "yearRangeEnd"
  | "onConfirm"
  | "onCancel"
  | "onDateChange"
  | "titleText"
> & {
  /** Controls the visibility of the date picker dialog. */
  visible?: boolean;
  /** The selected date value. Accepts Date, epoch milliseconds, or ISO string. */
  value?: Date | number | string | null;
  /** The initially displayed month. Accepts Date, epoch milliseconds, or ISO string. */
  initialDisplayedMonth?: Date | number | string;
  /** Minimum selectable date. Accepts Date, epoch milliseconds, or ISO string. */
  minDate?: Date | number | string;
  /** Maximum selectable date. Accepts Date, epoch milliseconds, or ISO string. */
  maxDate?: Date | number | string;
  /** Year range for the year selector. */
  yearRange?: YearRange;
  /** Initial display mode: 'picker' (calendar) or 'input' (text field). */
  initialDisplayMode?: "picker" | "input";
  /** Whether to show the mode toggle button. Defaults to true. */
  showModeToggle?: boolean;
  /** Label for the confirm button. Defaults to "OK". */
  confirmLabel?: string;
  /** Label for the cancel button. Defaults to "Cancel". */
  cancelLabel?: string;
  /** Title text displayed at the top of the picker. */
  title?: string;
  /** Callback fired when user confirms selection with the selected date. */
  onConfirm?: (date: Date | null) => void;
  /** Callback fired when user cancels/dismisses the dialog. */
  onCancel?: () => void;
  /** Callback fired when the selected date changes (before confirmation). */
  onChange?: (date: Date | null) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

/**
 * A modal date picker dialog powered by Jetpack Compose's DatePickerDialog.
 *
 * @example
 * ```tsx
 * <DatePickerModal
 *   visible={isOpen}
 *   value={selectedDate}
 *   onConfirm={(date) => {
 *     setSelectedDate(date);
 *     setIsOpen(false);
 *   }}
 *   onCancel={() => setIsOpen(false)}
 * />
 * ```
 */
export const DatePickerModal: FunctionComponent<DatePickerModalProps> = ({
  visible = false,
  value,
  initialDisplayedMonth,
  minDate,
  maxDate,
  yearRange,
  initialDisplayMode = "picker",
  showModeToggle = true,
  confirmLabel,
  cancelLabel,
  title,
  onConfirm,
  onCancel,
  onChange,
  style,
  ...props
}) => {
  // Convert dates to epoch milliseconds
  const selectedDateMillis = useMemo(() => toEpochMillis(value), [value]);
  const initialDisplayedMonthMillis = useMemo(
    () => toEpochMillis(initialDisplayedMonth),
    [initialDisplayedMonth],
  );
  const minDateMillis = useMemo(() => toEpochMillis(minDate), [minDate]);
  const maxDateMillis = useMemo(() => toEpochMillis(maxDate), [maxDate]);

  const handleConfirm = useCallback(
    (event: NativeSyntheticEvent<ConfirmEvent>) => {
      const millis = event.nativeEvent.selectedDateMillis;
      onConfirm?.(millis ? new Date(millis) : null);
    },
    [onConfirm],
  );

  const handleCancel = useCallback(
    (_event: NativeSyntheticEvent<CancelEvent>) => {
      onCancel?.();
    },
    [onCancel],
  );

  const handleDateChange = useCallback(
    (event: NativeSyntheticEvent<DateChangeEvent>) => {
      const millis = event.nativeEvent.selectedDateMillis;
      onChange?.(millis ? new Date(millis) : null);
    },
    [onChange],
  );

  // Don't render the native view when not visible to avoid blocking touches
  if (!visible) {
    return null;
  }

  return (
    <DatePickerNativeComponent
      {...props}
      isInline={false}
      visible={visible}
      selectedDateMillis={selectedDateMillis}
      initialDisplayedMonthMillis={initialDisplayedMonthMillis}
      minDateMillis={minDateMillis}
      maxDateMillis={maxDateMillis}
      yearRangeStart={yearRange?.start}
      yearRangeEnd={yearRange?.end}
      initialDisplayMode={initialDisplayMode}
      showModeToggle={showModeToggle}
      confirmLabel={confirmLabel}
      cancelLabel={cancelLabel}
      titleText={title}
      onConfirm={handleConfirm}
      onCancel={handleCancel}
      onDateChange={handleDateChange}
      style={[styles.base, style]}
    />
  );
};

const styles = StyleSheet.create({
  base: {
    position: "absolute",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  },
});
