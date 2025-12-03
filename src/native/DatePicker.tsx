import React, { FunctionComponent, useCallback, useMemo } from "react";
import type { NativeSyntheticEvent, StyleProp, ViewStyle } from "react-native";
import { StyleSheet } from "react-native";

import DatePickerNativeComponent, {
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
 * Props for the DatePicker component (inline mode).
 */
export type DatePickerProps = Omit<
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
  | "confirmLabel"
  | "cancelLabel"
> & {
  /** Controls the visibility of the date picker. */
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
  /** Title text displayed at the top of the picker. */
  title?: string;
  /** Callback fired when the selected date changes. */
  onChange?: (date: Date | null) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

/**
 * An inline date picker component powered by Jetpack Compose's DatePicker.
 *
 * @example
 * ```tsx
 * <DatePicker
 *   visible
 *   value={selectedDate}
 *   minDate={new Date()}
 *   onChange={(date) => setSelectedDate(date)}
 * />
 * ```
 */
export const DatePicker: FunctionComponent<DatePickerProps> = ({
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
  const selectedDateMillis = useMemo(() => toEpochMillis(value), [value]);
  const initialDisplayedMonthMillis = useMemo(
    () => toEpochMillis(initialDisplayedMonth),
    [initialDisplayedMonth],
  );
  const minDateMillis = useMemo(() => toEpochMillis(minDate), [minDate]);
  const maxDateMillis = useMemo(() => toEpochMillis(maxDate), [maxDate]);

  const handleDateChange = useCallback(
    (event: NativeSyntheticEvent<DateChangeEvent>) => {
      const millis = event.nativeEvent.selectedDateMillis;
      onChange?.(millis ? new Date(millis) : null);
    },
    [onChange],
  );

  // For inline mode, we don't need onConfirm/onCancel - changes are immediate
  const handleConfirm = useCallback(
    (event: NativeSyntheticEvent<ConfirmEvent>) => {
      const millis = event.nativeEvent.selectedDateMillis;
      onChange?.(millis ? new Date(millis) : null);
    },
    [onChange],
  );

  return (
    <DatePickerNativeComponent
      {...props}
      isInline={true}
      visible={visible}
      selectedDateMillis={selectedDateMillis}
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
