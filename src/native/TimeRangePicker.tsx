import React, { FunctionComponent, useCallback, useMemo } from "react";
import { StyleSheet, type NativeSyntheticEvent, type StyleProp, type ViewStyle } from "react-native";

import TimeRangePickerNativeComponent, {
  type NativeTimeRangePickerProps,
  type RangeCancelEvent,
  type RangeConfirmEvent,
  type RangeTimeChangeEvent,
} from "./TimeRangePickerNativeComponent";

/**
 * Converts a Date to minutes from midnight (0-1439).
 */
function toMinutesFromMidnight(time: Date | undefined | null): number | undefined {
  if (time == null) return undefined;
  return time.getHours() * 60 + time.getMinutes();
}

/**
 * Creates a Date object with today's date and the given minutes from midnight.
 */
function fromMinutesFromMidnight(minutes: number | undefined | null): Date | null {
  if (minutes == null) return null;
  const date = new Date();
  date.setHours(Math.floor(minutes / 60));
  date.setMinutes(minutes % 60);
  date.setSeconds(0);
  date.setMilliseconds(0);
  return date;
}

/**
 * Represents a time range with start and end times.
 */
export type TimeRange = {
  startTime: Date | null;
  endTime: Date | null;
};

/**
 * Props for the TimeRangePicker component.
 */
export type TimeRangePickerProps = Omit<
  NativeTimeRangePickerProps,
  "startTimeMinutes" | "endTimeMinutes" | "onConfirm" | "onCancel" | "onTimeChange" | "titleText"
> & {
  /** The selected time range value. */
  value?: TimeRange | null;
  /** Whether to use 24-hour format. If not set, follows system locale. */
  is24Hour?: boolean;
  /** Initial display mode: 'picker' (dial) or 'input' (keyboard). */
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
  /** Placeholder text when no time range is selected. */
  placeholder?: string;
  /** Whether the picker field is disabled. */
  disabled?: boolean;
  /** Callback fired when user confirms selection with the selected time range. */
  onConfirm?: (range: TimeRange) => void;
  /** Callback fired when user cancels/dismisses the dialog. */
  onCancel?: () => void;
  /** Callback fired when the selected time range changes (before confirmation). */
  onChange?: (range: TimeRange) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

/**
 * A time range picker component with a text field that opens a time range picker dialog.
 *
 * Renders an OutlinedTextField that displays the selected time range. When clicked,
 * opens a TimeRangePickerDialog for time range selection.
 *
 * @example
 * ```tsx
 * <TimeRangePicker
 *   value={{ startTime, endTime }}
 *   label="Working Hours"
 *   placeholder="Select time range"
 *   onConfirm={(range) => {
 *     setStartTime(range.startTime);
 *     setEndTime(range.endTime);
 *   }}
 *   onCancel={() => console.log('Cancelled')}
 * />
 * ```
 */
export const TimeRangePicker: FunctionComponent<TimeRangePickerProps> = ({
  value,
  is24Hour,
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
  // Convert times to minutes from midnight
  const startTimeMinutes = useMemo(() => toMinutesFromMidnight(value?.startTime), [value?.startTime]);
  const endTimeMinutes = useMemo(() => toMinutesFromMidnight(value?.endTime), [value?.endTime]);

  const handleConfirm = useCallback(
    (event: NativeSyntheticEvent<RangeConfirmEvent>) => {
      const { startTimeMinutes: start, endTimeMinutes: end } = event.nativeEvent;
      onConfirm?.({
        startTime: fromMinutesFromMidnight(start),
        endTime: fromMinutesFromMidnight(end),
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

  const handleTimeChange = useCallback(
    (event: NativeSyntheticEvent<RangeTimeChangeEvent>) => {
      const { startTimeMinutes: start, endTimeMinutes: end } = event.nativeEvent;
      onChange?.({
        startTime: fromMinutesFromMidnight(start),
        endTime: fromMinutesFromMidnight(end),
      });
    },
    [onChange],
  );

  return (
    <TimeRangePickerNativeComponent
      {...props}
      startTimeMinutes={startTimeMinutes}
      endTimeMinutes={endTimeMinutes}
      is24Hour={is24Hour}
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
      onTimeChange={handleTimeChange}
      style={[styles.base, style]}
    />
  );
};

const styles = StyleSheet.create({
  base: {
    minHeight: 64,
  },
});
