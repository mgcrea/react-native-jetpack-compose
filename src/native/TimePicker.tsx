import React, { FunctionComponent, useCallback, useMemo } from "react";
import { StyleSheet, type NativeSyntheticEvent, type StyleProp, type ViewStyle } from "react-native";

import TimePickerNativeComponent, {
  type CancelEvent,
  type ConfirmEvent,
  type NativeTimePickerProps,
  type TimeChangeEvent,
} from "./TimePickerNativeComponent";

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
 * Props for the TimePicker component.
 */
export type TimePickerProps = Omit<
  NativeTimePickerProps,
  "selectedTimeMinutes" | "onConfirm" | "onCancel" | "onTimeChange" | "titleText"
> & {
  /** The selected time value. */
  value?: Date | null;
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
  /** Placeholder text when no time is selected. */
  placeholder?: string;
  /** Whether the picker field is disabled. */
  disabled?: boolean;
  /** Callback fired when user confirms selection with the selected time. */
  onConfirm?: (time: Date | null) => void;
  /** Callback fired when user cancels/dismisses the dialog. */
  onCancel?: () => void;
  /** Callback fired when the selected time changes (before confirmation). */
  onChange?: (time: Date | null) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

/**
 * A time picker component with a text field that opens a time picker dialog.
 *
 * Renders an OutlinedTextField that displays the selected time. When clicked,
 * opens a TimePickerDialog for time selection.
 *
 * @example
 * ```tsx
 * <TimePicker
 *   value={selectedTime}
 *   label="Meeting Time"
 *   placeholder="Select a time"
 *   onConfirm={(time) => setSelectedTime(time)}
 *   onCancel={() => console.log('Cancelled')}
 * />
 * ```
 */
export const TimePicker: FunctionComponent<TimePickerProps> = ({
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
  // Convert time to minutes from midnight
  const selectedTimeMinutes = useMemo(() => toMinutesFromMidnight(value), [value]);

  const handleConfirm = useCallback(
    (event: NativeSyntheticEvent<ConfirmEvent>) => {
      const minutes = event.nativeEvent.selectedTimeMinutes;
      onConfirm?.(fromMinutesFromMidnight(minutes));
    },
    [onConfirm],
  );

  const handleCancel = useCallback(
    (_event: NativeSyntheticEvent<CancelEvent>) => {
      onCancel?.();
    },
    [onCancel],
  );

  const handleTimeChange = useCallback(
    (event: NativeSyntheticEvent<TimeChangeEvent>) => {
      const minutes = event.nativeEvent.selectedTimeMinutes;
      onChange?.(fromMinutesFromMidnight(minutes));
    },
    [onChange],
  );

  return (
    <TimePickerNativeComponent
      {...props}
      selectedTimeMinutes={selectedTimeMinutes}
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
