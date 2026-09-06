import type { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";

import { useNativeEvent, useVoidEvent } from "../hooks/useNativeEvent";
import type { TimeRange } from "../types";
import { fromMinutesFromMidnight, toMinutesFromMidnight } from "../utils/date";
import { fieldStyles, useFieldStyle } from "../utils/style";
import TimeRangePickerNativeComponent, {
  type NativeTimeRangePickerProps,
  type RangeConfirmEvent,
  type RangeTimeChangeEvent,
} from "./TimeRangePickerNativeComponent";

export type { TimeRange } from "../types";

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

const toTimeRange = (event: RangeConfirmEvent | RangeTimeChangeEvent): TimeRange => {
  // Share one base day so both ends of the range land on the same date
  const base = new Date();
  return {
    startTime: fromMinutesFromMidnight(event.startTimeMinutes, base),
    endTime: fromMinutesFromMidnight(event.endTimeMinutes, base),
  };
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
  const handleConfirm = useNativeEvent(onConfirm, toTimeRange);
  const handleCancel = useVoidEvent(onCancel);
  const handleTimeChange = useNativeEvent(onChange, toTimeRange);

  return (
    <TimeRangePickerNativeComponent
      {...props}
      startTimeMinutes={toMinutesFromMidnight(value?.startTime)}
      endTimeMinutes={toMinutesFromMidnight(value?.endTime)}
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
      style={useFieldStyle(fieldStyles.field, style)}
    />
  );
};
