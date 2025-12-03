import React, { FunctionComponent, useCallback, useMemo } from "react";
import type { NativeSyntheticEvent, StyleProp, ViewStyle } from "react-native";
import { StyleSheet } from "react-native";

import DateRangePickerNativeComponent, {
  type RangeConfirmEvent,
  type RangeDateChangeEvent,
  type NativeDateRangePickerProps,
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
export interface YearRange {
  start: number;
  end: number;
}

/**
 * Represents a date range with start and end dates.
 */
export interface DateRange {
  startDate: Date | null;
  endDate: Date | null;
}

/**
 * Props for the DateRangePicker component (inline mode).
 */
export type DateRangePickerProps = Omit<
  NativeDateRangePickerProps,
  | "isInline"
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
  | "confirmLabel"
  | "cancelLabel"
> & {
  /** Controls the visibility of the date range picker. */
  visible?: boolean;
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
  /** Title text displayed at the top of the picker. */
  title?: string;
  /** Callback fired when the selected date range changes. */
  onChange?: (range: DateRange) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

/**
 * An inline date range picker component powered by Jetpack Compose's DateRangePicker.
 *
 * @example
 * ```tsx
 * <DateRangePicker
 *   visible
 *   value={{ startDate, endDate }}
 *   minDate={new Date()}
 *   onChange={(range) => {
 *     setStartDate(range.startDate);
 *     setEndDate(range.endDate);
 *   }}
 * />
 * ```
 */
export const DateRangePicker: FunctionComponent<DateRangePickerProps> = ({
  visible = true,
  value,
  initialDisplayedMonth,
  minDate,
  maxDate,
  yearRange,
  initialDisplayMode = "picker",
  showModeToggle = true,
  title,
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

  // For inline mode, we don't need onConfirm/onCancel - changes are immediate
  const handleConfirm = useCallback(
    (event: NativeSyntheticEvent<RangeConfirmEvent>) => {
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
      isInline={true}
      visible={visible}
      startDateMillis={startDateMillis}
      endDateMillis={endDateMillis}
      initialDisplayedMonthMillis={initialDisplayedMonthMillis}
      minDateMillis={minDateMillis}
      maxDateMillis={maxDateMillis}
      yearRangeStart={yearRange?.start}
      yearRangeEnd={yearRange?.end}
      initialDisplayMode={initialDisplayMode}
      showModeToggle={showModeToggle}
      titleText={title}
      onDateChange={handleDateChange}
      onConfirm={handleConfirm}
      style={[styles.base, style]}
    />
  );
};

const styles = StyleSheet.create({
  base: {
    minHeight: 400,
  },
});
