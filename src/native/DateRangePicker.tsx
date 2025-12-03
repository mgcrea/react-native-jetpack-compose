import React, { FunctionComponent, useCallback, useMemo } from "react";
import { StyleSheet, type NativeSyntheticEvent, type StyleProp, type ViewStyle } from "react-native";

import DateRangePickerNativeComponent, {
  type NativeDateRangePickerProps,
  type RangeCancelEvent,
  type RangeConfirmEvent,
  type RangeDateChangeEvent,
} from "./DateRangePickerNativeComponent";

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
export type YearRange = {
  start: number;
  end: number;
};

/**
 * Represents a date range with start and end dates.
 */
export type DateRange = {
  startDate: Date | null;
  endDate: Date | null;
};

/**
 * Props for the DateRangePicker component.
 */
export type DateRangePickerProps = Omit<
  NativeDateRangePickerProps,
  | "startDateMillis"
  | "endDateMillis"
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
  /** The selected date range value. */
  value?: DateRange | null;
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
  /** Title text displayed at the top of the picker dialog. */
  title?: string;
  /** Floating label text for the text field. */
  label?: string;
  /** Placeholder text when no date range is selected. */
  placeholder?: string;
  /** Whether the picker field is disabled. */
  disabled?: boolean;
  /** Callback fired when user confirms selection with the selected date range. */
  onConfirm?: (range: DateRange) => void;
  /** Callback fired when user cancels/dismisses the dialog. */
  onCancel?: () => void;
  /** Callback fired when the selected date range changes (before confirmation). */
  onChange?: (range: DateRange) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

/**
 * A date range picker component with a text field that opens a date range picker dialog.
 *
 * Renders an OutlinedTextField that displays the selected date range. When clicked,
 * opens a DateRangePickerDialog for date range selection.
 *
 * @example
 * ```tsx
 * <DateRangePicker
 *   value={{ startDate, endDate }}
 *   label="Trip Dates"
 *   placeholder="Select dates"
 *   onConfirm={(range) => {
 *     setStartDate(range.startDate);
 *     setEndDate(range.endDate);
 *   }}
 *   onCancel={() => console.log('Cancelled')}
 * />
 * ```
 */
export const DateRangePicker: FunctionComponent<DateRangePickerProps> = ({
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
  label,
  placeholder,
  disabled,
  onConfirm,
  onCancel,
  onChange,
  style,
  ...props
}) => {
  // Convert dates to epoch milliseconds
  const startDateMillis = useMemo(() => toEpochMillis(value?.startDate), [value?.startDate]);
  const endDateMillis = useMemo(() => toEpochMillis(value?.endDate), [value?.endDate]);
  const initialDisplayedMonthMillis = useMemo(
    () => toEpochMillis(initialDisplayedMonth),
    [initialDisplayedMonth],
  );
  const minDateMillis = useMemo(() => toEpochMillis(minDate), [minDate]);
  const maxDateMillis = useMemo(() => toEpochMillis(maxDate), [maxDate]);

  const handleConfirm = useCallback(
    (event: NativeSyntheticEvent<RangeConfirmEvent>) => {
      const { startDateMillis: start, endDateMillis: end } = event.nativeEvent;
      onConfirm?.({
        startDate: start ? new Date(start) : null,
        endDate: end ? new Date(end) : null,
      });
    },
    [onConfirm],
  );

  const handleCancel = useCallback(
    (_event: NativeSyntheticEvent<RangeCancelEvent>) => {
      onCancel?.();
    },
    [onCancel],
  );

  const handleDateChange = useCallback(
    (event: NativeSyntheticEvent<RangeDateChangeEvent>) => {
      const { startDateMillis: start, endDateMillis: end } = event.nativeEvent;
      onChange?.({
        startDate: start ? new Date(start) : null,
        endDate: end ? new Date(end) : null,
      });
    },
    [onChange],
  );

  return (
    <DateRangePickerNativeComponent
      {...props}
      startDateMillis={startDateMillis}
      endDateMillis={endDateMillis}
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
      label={label}
      placeholder={placeholder}
      disabled={disabled}
      onConfirm={handleConfirm}
      onCancel={handleCancel}
      onDateChange={handleDateChange}
      style={[styles.base, style]}
    />
  );
};

const styles = StyleSheet.create({
  base: {
    minHeight: 64,
  },
});
